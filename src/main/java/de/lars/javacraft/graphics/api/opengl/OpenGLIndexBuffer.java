package de.lars.javacraft.graphics.api.opengl;

import de.lars.javacraft.graphics.api.Buffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDeleteBuffers;

public class OpenGLIndexBuffer extends OpenGLBuffer {
   public OpenGLIndexBuffer(long sizeInBytes) {
       super(sizeInBytes, GL_ELEMENT_ARRAY_BUFFER);
   }
}
