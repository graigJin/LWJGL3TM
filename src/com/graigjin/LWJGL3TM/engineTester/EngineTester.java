package com.graigjin.LWJGL3TM.engineTester;

import com.graigjin.LWJGL3TM.entities.Camera;
import com.graigjin.LWJGL3TM.entities.Entity;
import com.graigjin.LWJGL3TM.entities.Light;
import com.graigjin.LWJGL3TM.models.RawModel;
import com.graigjin.LWJGL3TM.models.TexturedModel;
import com.graigjin.LWJGL3TM.renderEngine.*;
import com.graigjin.LWJGL3TM.shaders.StaticShader;
import com.graigjin.LWJGL3TM.textures.ModelTexture;
import org.joml.Vector3f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class EngineTester {

    public static void main (String[] args) throws IOException {
        DisplayManager.createDisplay(1200, 720, "Display");

        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("banana"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture.setShineDamper(10);
        texture.setReflectivity(0.25f);

        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-25),0,0,0,1);
        Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();

        while(!glfwWindowShouldClose(DisplayManager.getWindow())) {
            entity.increaseRotation(0,1,0);
            camera.move();

            renderer.processEntity(entity);

            renderer.render(light, camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }

}
