package de.lars.javacraft.graphics.opengl;

import de.lars.javacraft.graphics.Shader;

import static org.lwjgl.opengl.GL20.*;

public class OpenGLShader implements Shader {
    private int m_Id;

    /**
     * Links and Compiles provided Shaders into Shader Program
     * @param vertexShader Source of Vertex Shader
     * @param fragmentShader Source of Fragment Shader
     */
    public OpenGLShader(String vertexShader, String fragmentShader) {
        m_Id = glCreateProgram();
        int vs = compileShader(vertexShader, Type.VERTEX);
        int fs = compileShader(fragmentShader, Type.FRAGMENT);

        glAttachShader(m_Id, vs);
        glAttachShader(m_Id, fs);
        glLinkProgram(m_Id);

        int status = glGetProgrami(m_Id, GL_LINK_STATUS);
        if(status == GL_FALSE) {
            String log = glGetProgramInfoLog(m_Id);
            throw new RuntimeException("Could not link shaders:\n "+ log);
        }

        //Delete unneeded shaders
        glDeleteShader(vs);
        glDeleteShader(fs);
    }

    /**
     * Compiles Shader and checks it for errors
     * @param source Source of the Shader
     * @param type Type of the Shader
     * @return Handle to compiled Shader
     */
    private int compileShader(String source, Shader.Type type) {
        int shader = glCreateShader(ShaderTypeToNative(type));

        glShaderSource(shader, source);
        glCompileShader(shader);

        int status = glGetShaderi(shader, GL_COMPILE_STATUS);
        if(status == GL_FALSE) {
            String log = glGetShaderInfoLog(shader);
            throw new RuntimeException("Could not compile " + type + " shader:\n" + log);
        }

        return shader;
    }

    /**
     * Converts Shader.Type to native OpenGL Shader Type
     * @param type Enum to convert
     * @return GL_*_SHADER
     */
    private int ShaderTypeToNative(Shader.Type type) {
        return switch (type) {
            case FRAGMENT -> GL_FRAGMENT_SHADER;
            case VERTEX -> GL_VERTEX_SHADER;
        };
    }

    @Override
    public void setInt(String name, int value) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public void use() {
        glUseProgram(m_Id);
    }

    @Override
    public void dispose() {
        if(m_Id != -1) {
            glDeleteProgram(m_Id);
            m_Id = -1;
        }
    }
}
