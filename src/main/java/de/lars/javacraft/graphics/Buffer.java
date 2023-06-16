package de.lars.javacraft.graphics;

import de.lars.javacraft.graphics.opengl.OpenGLBuffer;

public interface Buffer extends GPUResource {
    static Buffer createVertexBuffer(long sizeInBytes) {
        return switch (RenderAPI.current()) {
            case NONE -> throw new UnsupportedOperationException("RenderAPI.None is not supported yet");
            case OPENGL_4 -> new OpenGLBuffer(sizeInBytes);
        };
    }

    void upload(float[] data, long offset);
    void use();
}
