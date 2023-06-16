package de.lars.javacraft.graphics.opengl;

import de.lars.javacraft.graphics.Buffer;

import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDeleteBuffers;

public class OpenGLBuffer implements Buffer {
    private int m_Id;

    public OpenGLBuffer(long sizeInBytes) {
        m_Id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, m_Id);
        glBufferData(GL_ARRAY_BUFFER, sizeInBytes, GL_DYNAMIC_DRAW);
    }

    @Override
    public void upload(float[] data, long offset) {
        glBindBuffer(GL_ARRAY_BUFFER, m_Id);
        glBufferSubData(GL_ARRAY_BUFFER, offset, data);
    }

    @Override
    public void use() {
        glBindBuffer(GL_ARRAY_BUFFER, m_Id);
    }

    @Override
    public void dispose() {
        if(m_Id != -1) {
            glDeleteBuffers(m_Id);
            m_Id = -1;
        }
    }
}
