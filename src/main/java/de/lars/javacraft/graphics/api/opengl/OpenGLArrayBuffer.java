package de.lars.javacraft.graphics.api.opengl;

import de.lars.javacraft.graphics.api.Buffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDeleteBuffers;

public class OpenGLArrayBuffer extends OpenGLBuffer {
   public OpenGLArrayBuffer(long sizeInBytes) {
       super(sizeInBytes, GL_ARRAY_BUFFER);
   }
}
