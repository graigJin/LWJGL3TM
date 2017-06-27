package com.graigjin.LWJGL3TM.renderEngine;

import com.graigjin.LWJGL3TM.entities.Entity;
import com.graigjin.LWJGL3TM.models.RawModel;
import com.graigjin.LWJGL3TM.models.TexturedModel;
import com.graigjin.LWJGL3TM.shaders.StaticShader;
import com.graigjin.LWJGL3TM.toolbox.Maths;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader) {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare() {
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.0f, 0.4f, 0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Entity entity, StaticShader shader) {
        TexturedModel texturedModel = entity.getModel();
        RawModel model = texturedModel.getRawModel();

        glBindVertexArray(model.getVaoID());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(),
                entity.getScale()
        );
        shader.loadTransformationMatrix(transformationMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getID());

        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    private void createProjectionMatrix() {
        IntBuffer w = BufferUtils.createIntBuffer(4);
        IntBuffer h = BufferUtils.createIntBuffer(4);
        glfwGetWindowSize(DisplayManager.getWindow(), w, h);
        int width = w.get();
        int height = h.get();

        float aspectRatio = (float) width / (float) height;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }
}
