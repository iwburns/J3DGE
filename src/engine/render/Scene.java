package engine.render;

import engine.object3d.Object3d;
import engine.object3d.light.Light;

import java.util.ArrayList;

public class Scene {

    //TODO: write a destroy

    private ArrayList<Object3d> objects;
    private ArrayList<Light> lights;

    public Scene() {
        objects = new ArrayList<>();
        lights = new ArrayList<>();
    }

    public void add(Object3d obj) {
        objects.add(obj);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public boolean remove(Object3d obj) {
        return objects.remove(obj);
    }

    public boolean remove(Light light) {
        return lights.remove(light);
    }

    public ArrayList<Object3d> getObjects() {
        return objects;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

}
