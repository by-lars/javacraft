package de.lars.javacraft.graphics.api;

import de.lars.javacraft.graphics.api.opengl.OpenGLShader;
import de.lars.javacraft.util.Resource;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public interface Shader extends GPUResource {
    enum Type {
        VERTEX,
        FRAGMENT
    }

    /**
     * Create a new Shader Program from the provided resource files
     * @param vsPath Path to Vertex Shader File
     * @param fsPath Path to Fragment Shader File
     * @return new Shader Program
     */
    static Shader fromFiles(String vsPath, String fsPath) {
        String vertexShader;
        String fragmentShader;

        try {
            vertexShader = Resource.asString(vsPath);
            fragmentShader = Resource.asString(fsPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not find file: " + e.getMessage());
        }

        return switch (RenderAPI.current()) {
            case NONE -> throw new UnsupportedOperationException("RenderAPI.None is not supported yet");
            case OPENGL_4 -> new OpenGLShader(vertexShader, fragmentShader);
        };
    }

    /**
     * Create a new Shader Program from the provided sources
     * @param vertexShader The source code of the vertex Shader
     * @param fragmentShader The source code of the fragment Shader
     * @return new Shader Program
     */
    static Shader fromSources(String vertexShader, String fragmentShader) {
        return switch (RenderAPI.current()) {
            case NONE -> throw new UnsupportedOperationException("RenderAPI.None is not supported yet");
            case OPENGL_4 -> new OpenGLShader(vertexShader, fragmentShader);
        };
    }

    /**
     * Sets the Shader as current
     */
    void use();

    /**
     * Sets a Shader Uniform Int Value
     * @param name Name of the Uniform
     * @param value Value to be set
     */
    void setInt(String name, int value);

    /**
     * Sets a Shader Uniform Matrix
     * @param name Name of the Uniform
     * @param matrix Matrix to be set
     */
    void setMat4f(String name, Matrix4f matrix);
}
