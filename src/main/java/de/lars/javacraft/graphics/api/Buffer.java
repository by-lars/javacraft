package de.lars.javacraft.graphics.api;

import de.lars.javacraft.graphics.api.opengl.OpenGLArrayBuffer;
import de.lars.javacraft.graphics.api.opengl.OpenGLIndexBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface Buffer extends GPUResource {
    static Buffer createArrayBuffer(long sizeInBytes) {
        return switch (RenderAPI.current()) {
            case NONE -> throw new UnsupportedOperationException("RenderAPI.None is not supported yet");
            case OPENGL_4 -> new OpenGLArrayBuffer(sizeInBytes);
        };
    }

    static Buffer createIndexBuffer(long sizeInBytes) {
        return switch (RenderAPI.current()) {
            case NONE -> throw new UnsupportedOperationException("RenderAPI.None is not supported yet");
            case OPENGL_4 -> new OpenGLIndexBuffer(sizeInBytes);
        };
    }

    void upload(float[] data, long offset);
    void upload(FloatBuffer data, long offset);

    void upload(int[] data, long offset);
    void upload(IntBuffer data, long offset);


    void use();
}
