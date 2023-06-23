package de.lars.javacraft.graphics.api.opengl;

import de.lars.javacraft.graphics.api.Buffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class OpenGLBuffer implements Buffer {
    private int m_Id;
    private int m_Type;

    public OpenGLBuffer(long sizeInBytes, int type) {
        m_Id = glGenBuffers();
        m_Type = type;
        glBindBuffer(m_Type, m_Id);
        glBufferData(m_Type, sizeInBytes, GL_DYNAMIC_DRAW);
    }

    @Override
    public void upload(float[] data, long offset) {
        glBindBuffer(m_Type, m_Id);
        glBufferSubData(m_Type, offset, data);
    }

    @Override
    public void upload(FloatBuffer data, long offset) {
        data.flip();
        glBindBuffer(m_Type, m_Id);
        glBufferSubData(m_Type, offset, data);
    }

    @Override
    public void upload(int[] data, long offset) {
        glBindBuffer(m_Type, m_Id);
        glBufferSubData(m_Type, offset, data);
    }

    @Override
    public void upload(IntBuffer data, long offset) {
        data.flip();
        glBindBuffer(m_Type, m_Id);
        glBufferSubData(m_Type, offset, data);
    }

    @Override
    public void use() {
        glBindBuffer(m_Type, m_Id);
    }

    @Override
    public void dispose() {
        if(m_Id != -1) {
            glDeleteBuffers(m_Id);
            m_Id = -1;
        }
    }
}
