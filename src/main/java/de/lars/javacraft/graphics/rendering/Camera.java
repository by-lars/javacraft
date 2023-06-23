package de.lars.javacraft.graphics.rendering;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {
    private float m_Pitch;
    private float m_Yaw;
    private float m_LastX;
    private float m_LastY;
    private Vector3f m_Pos;

    private Quaternionf m_Orientation;
    private Matrix4f m_ViewMatrix;

    public Camera(Vector3f pos) {
        m_Pos = pos;
        m_Yaw = 0.0f;
        m_Pitch = 0.0f;
        m_LastX = 0.0f;
        m_LastY = 0.0f;
        m_ViewMatrix = new Matrix4f();
        m_Orientation = new Quaternionf();
    }

    public Matrix4f getViewMatrix() {
        return m_ViewMatrix;
    }

    public void setPosition(Vector3f pos) {
        m_Pos = pos;
        updateVectors();
    }

    public void translate(Vector3f trans) {
        m_Pos.add(trans);
        updateVectors();
    }

    public void rotate(float x, float y) {
        float deltaX = x - m_LastX;
        float deltaY = y - m_LastY;

        m_Yaw += deltaX * 0.5f;
        m_Pitch += deltaY * 0.5f;

        m_LastX = x;
        m_LastY = y;

        if(m_Pitch > 88.0f)
            m_Pitch = 88.0f;
        if(m_Pitch < -88.0f)
            m_Pitch = -88.0f;

        updateVectors();
    }

    public Quaternionf getOrientation() {
        return m_Orientation;
    }

    private void updateVectors() {
        float pitch = (float)Math.toRadians(m_Pitch);
        float yaw = (float)Math.toRadians(m_Yaw);

        var qPitch = new Quaternionf(new AxisAngle4f(pitch, new Vector3f(1,0,0)));
        var qYaw =  new Quaternionf(new AxisAngle4f(yaw, new Vector3f(0,1,0)));

        m_Orientation = qPitch.mul(qYaw);
        m_Orientation.normalize();

        Matrix4f rotation = new Matrix4f();
        rotation.rotate(m_Orientation);

        Matrix4f translation = new Matrix4f();
        Vector3f pos = new Vector3f(m_Pos);
        pos.mul(-1);
        translation.translate(pos);

        m_ViewMatrix = rotation.mul(translation);
    }
}
