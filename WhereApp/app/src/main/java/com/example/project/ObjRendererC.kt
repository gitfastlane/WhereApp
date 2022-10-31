package com.example.project

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.Log
import de.javagl.obj.Obj
import de.javagl.obj.ObjData
import de.javagl.obj.ObjReader
import de.javagl.obj.ObjUtils
import java.io.IOException

class ObjRendererC(private val mContext: Context, private val mObjName: String, private val mTextureName: String
) {
    private val vertexShaderString = """uniform mat4 uMvMatrix;
uniform mat4 uMvpMatrix;
attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec2 aTexCoord;
varying vec3 vPosition;
varying vec3 vNormal;
varying vec2 vTexCoord;
void main() {
   vPosition = (uMvMatrix * aPosition).xyz;
   vNormal = normalize((uMvMatrix * vec4(aNormal, 0.0)).xyz);
   vTexCoord = aTexCoord;
   gl_Position = uMvpMatrix * vec4(aPosition.xyz, 1.0);
}"""
    private val fragmentShaderString = """precision mediump float;
uniform sampler2D uTexture;
uniform vec4 uLighting;
uniform vec4 uMaterial;
uniform vec4 uColorCorrection;
varying vec3 vPosition;
varying vec3 vNormal;
varying vec2 vTexCoord;
void main() {
    const float kGamma = 0.4545454;
    const float kInverseGamma = 2.2;
    const float kMiddleGrayGamma = 0.466;
    vec3 viewLightDirection = uLighting.xyz;
    vec3 colorShift = uColorCorrection.rgb;
    float averagePixelIntensity = uColorCorrection.a;
    float lightIntensity = uLighting.w;
    float materialAmbient = uMaterial.x;
    float materialDiffuse = uMaterial.y;
    float materialSpecular = uMaterial.z;
    float materialSpecularPower = uMaterial.w;
    vec3 viewFragmentDirection = normalize(vPosition);
    vec3 viewNormal = normalize(vNormal);
    vec4 objectColor = texture2D(uTexture, vec2(vTexCoord.x, 1.0 - vTexCoord.y));
    objectColor.rgb = pow(objectColor.rgb, vec3(kInverseGamma));
    float ambient = materialAmbient;
    float diffuse = lightIntensity * materialDiffuse * 0.5 * (dot(viewNormal, viewLightDirection) + 1.0);
    vec3 reflectedLightDirection = reflect(viewLightDirection, viewNormal);
    float specularStrength = max(0.0, dot(viewFragmentDirection, reflectedLightDirection));
    float specular = lightIntensity * materialSpecular * pow(specularStrength, materialSpecularPower);
    vec3 color = objectColor.rgb * (ambient + diffuse) + specular;
    color.rgb = pow(color, vec3(kGamma));
    color *= colorShift * (averagePixelIntensity / 0.5);
    gl_FragColor.a = objectColor.a;
    gl_FragColor.rgb = color;
}"""
    private var mObj: Obj? = null
    private var mProgram = 0
    private var mTextures: IntArray? = null
    private var mVbos: IntArray? = null
    private var mVerticesBaseAddress = 0
    private var mTexCoordsBaseAddress = 0
    private var mNormalsBaseAddress = 0
    private var mIndicesCount = 0
    private val mModelMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mProjMatrix = FloatArray(16)

    //빛의 세기 변수
    private var mLightIntensity = 0f
    var mColorCorrection = floatArrayOf(0.8f, 0.8f, 0.8f, 0.8f)
    fun init() {
        try {
            val `is` = mContext.assets.open(mObjName)
            val bmp = BitmapFactory.decodeStream(mContext.assets.open(mTextureName))
            mObj = ObjReader.read(`is`)
            mObj = ObjUtils.convertToRenderable(mObj)
            mTextures = IntArray(1)
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glGenTextures(1, mTextures, 0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextures!![0])
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_LINEAR_MIPMAP_LINEAR
            )
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MAG_FILTER,
                GLES30.GL_LINEAR
            )
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bmp, 0)
            GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
            bmp.recycle()
        } catch (e: IOException) {
            Log.e(TAG, e.message!!)
        }
        if (mObj == null || mTextures!![0] == -1) {
            Log.e(TAG, "Failed to init obj - $mObjName, $mTextureName")
        }
        val indices = ObjData.convertToShortBuffer(ObjData.getFaceVertexIndices(mObj, 3))
        val vertices = ObjData.getVertices(mObj)
        val texCoords = ObjData.getTexCoords(mObj, 2)
        val normals = ObjData.getNormals(mObj)
        mVbos = IntArray(2)
        GLES30.glGenBuffers(2, mVbos, 0)
        mVerticesBaseAddress = 0
        mTexCoordsBaseAddress = mVerticesBaseAddress + 4 * vertices.limit()
        mNormalsBaseAddress = mTexCoordsBaseAddress + 4 * texCoords.limit()
        val totalBytes = mNormalsBaseAddress + 4 * normals.limit()
        mIndicesCount = indices.limit()

        // vertexBufferId
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVbos!![0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, totalBytes, null, GLES30.GL_STATIC_DRAW)
        GLES30.glBufferSubData(
            GLES30.GL_ARRAY_BUFFER,
            mVerticesBaseAddress,
            4 * vertices.limit(),
            vertices
        )
        GLES30.glBufferSubData(
            GLES30.GL_ARRAY_BUFFER,
            mTexCoordsBaseAddress,
            4 * texCoords.limit(),
            texCoords
        )
        GLES30.glBufferSubData(
            GLES30.GL_ARRAY_BUFFER,
            mNormalsBaseAddress,
            4 * normals.limit(),
            normals
        )
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        // indexBufferId
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVbos!![1])
        GLES30.glBufferData(
            GLES30.GL_ELEMENT_ARRAY_BUFFER,
            2 * mIndicesCount,
            indices,
            GLES30.GL_STATIC_DRAW
        )
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0)
        val vShader = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER)
        GLES30.glShaderSource(vShader, vertexShaderString)
        GLES30.glCompileShader(vShader)
        val compiled = IntArray(1)
        GLES30.glGetShaderiv(vShader, GLES30.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.e(TAG, "Could not compile vertex shader.")
            GLES30.glDeleteShader(vShader)
        }
        val fShader = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER)
        GLES30.glShaderSource(fShader, fragmentShaderString)
        GLES30.glCompileShader(fShader)
        GLES30.glGetShaderiv(fShader, GLES30.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.e(TAG, "Could not compile fragment shader.")
            GLES30.glDeleteShader(fShader)
        }
        mProgram = GLES30.glCreateProgram()
        GLES30.glAttachShader(mProgram, vShader)
        GLES30.glAttachShader(mProgram, fShader)
        GLES30.glLinkProgram(mProgram)
        val linked = IntArray(1)
        GLES30.glGetProgramiv(mProgram, GLES30.GL_LINK_STATUS, linked, 0)
        if (linked[0] == 0) {
            Log.e(TAG, "Could not link program.")
        }
    }

    fun draw() {
        val mvMatrix = FloatArray(16)
        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, mProjMatrix, 0, mvMatrix, 0)
        GLES30.glUseProgram(mProgram)
        val mv = GLES30.glGetUniformLocation(mProgram, "uMvMatrix")
        val mvp = GLES30.glGetUniformLocation(mProgram, "uMvpMatrix")
        val position = GLES30.glGetAttribLocation(mProgram, "aPosition")
        val normal = GLES30.glGetAttribLocation(mProgram, "aNormal")
        val texCoord = GLES30.glGetAttribLocation(mProgram, "aTexCoord")
        val texture = GLES30.glGetUniformLocation(mProgram, "uTexture")
        val lighting = GLES30.glGetUniformLocation(mProgram, "uLighting")
        val material = GLES30.glGetUniformLocation(mProgram, "uMaterial")
        val colorCorrection = GLES30.glGetUniformLocation(mProgram, "uColorCorrection")
        val viewLightDirection = FloatArray(4)
        val lightDirection = floatArrayOf(0.250f, 0.866f, 0.433f, 0.0f)
        Matrix.multiplyMV(viewLightDirection, 0, mvMatrix, 0, lightDirection, 0)
        normalize(viewLightDirection)
        // 빛의 세기를 받아 재질을 표현한다.
        GLES30.glUniform4f(
            lighting,
            viewLightDirection[0],
            viewLightDirection[1],
            viewLightDirection[2],
            mLightIntensity
        )
        GLES30.glUniform4f(
            colorCorrection,
            mColorCorrection[0],
            mColorCorrection[1],
            mColorCorrection[2],
            mColorCorrection[3]
        )
        val ambient = 0.3f
        val diffuse = 1.0f
        val specular = 1.0f
        val specularPower = 6.0f
        GLES30.glUniform4f(material, ambient, diffuse, specular, specularPower)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextures!![0])
        GLES30.glUniform1i(texture, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVbos!![0])
        GLES30.glVertexAttribPointer(position, 3, GLES30.GL_FLOAT, false, 0, mVerticesBaseAddress)
        GLES30.glVertexAttribPointer(normal, 3, GLES30.GL_FLOAT, false, 0, mNormalsBaseAddress)
        GLES30.glVertexAttribPointer(texCoord, 2, GLES30.GL_FLOAT, false, 0, mTexCoordsBaseAddress)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        GLES30.glUniformMatrix4fv(mv, 1, false, mvMatrix, 0)
        GLES30.glUniformMatrix4fv(mvp, 1, false, mvpMatrix, 0)
        GLES30.glEnableVertexAttribArray(position)
        GLES30.glEnableVertexAttribArray(normal)
        GLES30.glEnableVertexAttribArray(texCoord)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVbos!![1])
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, mIndicesCount, GLES30.GL_UNSIGNED_SHORT, 0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0)
        GLES30.glDisableVertexAttribArray(position)
        GLES30.glDisableVertexAttribArray(normal)
        GLES30.glDisableVertexAttribArray(texCoord)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
    }

    fun setModelMatrix(modelMatrix: FloatArray?) {
        System.arraycopy(modelMatrix, 0, mModelMatrix, 0, 16)
    }

    fun setProjectionMatrix(projMatrix: FloatArray?) {
        System.arraycopy(projMatrix, 0, mProjMatrix, 0, 16)
    }

    fun setViewMatrix(viewMatrix: FloatArray?) {
        System.arraycopy(viewMatrix, 0, mViewMatrix, 0, 16)
    }

    fun setLightIntensity(lightIntensity: Float) {
        mLightIntensity = lightIntensity
    }

    private fun normalize(v: FloatArray) {
        val norm = Math.sqrt((v[0] * v[0] + v[1] * v[1] + v[2] * v[2]).toDouble())
        v[0] /= norm.toFloat()
        v[1] /= norm.toFloat()
        v[2] /= norm.toFloat()
    }

    fun setColorCorrection(colorCorrection: FloatArray) {
        mColorCorrection[0] = colorCorrection[0]
        mColorCorrection[1] = colorCorrection[1]
        mColorCorrection[2] = colorCorrection[2]
        mColorCorrection[3] = colorCorrection[3]
    }

    companion object {
        private val TAG = ObjRendererC::class.java.simpleName
    }
}