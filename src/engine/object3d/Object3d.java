package engine.object3d;

import engine.object3d.light.Light;
import org.joml.*;

import java.util.ArrayList;

public class Object3d {
    
    private boolean localToWorldNeedsUpdate;
    private boolean worldToLocalNeedsUpdate;

    protected Vector3f position;
    protected Quaternionf rotation;
    protected Vector3f scale;

    protected Matrix4f localToWorld;
    protected Matrix4f worldToLocal;

    protected Object3d parent;
    protected ArrayList<Object3d> children;

    public Object3d() {
        localToWorldNeedsUpdate = true;
        worldToLocalNeedsUpdate = true;

        position = new Vector3f();
        rotation = new Quaternionf();
        scale = new Vector3f(1, 1, 1);

        localToWorld = new Matrix4f();
        worldToLocal = new Matrix4f();

        parent = null;
        children = new ArrayList<>();
    }

    private void setUpdateFlags(boolean b) {
        localToWorldNeedsUpdate = b;
        worldToLocalNeedsUpdate = b;

        children.forEach((child) -> child.setUpdateFlags(b));
    }

    public void addChild(Object3d obj) {
        if (this == obj) {
            throw new IllegalArgumentException("Cannot add object as a child to itself.");
        }
        if (obj instanceof Light) {
            Object3d currentObj = this;
            while (currentObj.getParent() != null) {
                currentObj = currentObj.getParent();
            }
            if (currentObj instanceof Scene) {
                // it should be
                ((Scene) currentObj).addLight((Light) obj);
            }
        }
        children.add(obj);
        obj.parent = this;
    }

    public void translate(Vector3f v) {
        position.add(v);
        setUpdateFlags(true);
    }

    public void translateRelativeToRotation(Vector3f v) {
        translate(rotation.transform(v));
    }

    public void moveForward(float amt) {
        translateRelativeToRotation(new Vector3f(0, 0, -amt));
    }

    public void moveRight(float amt) {
        translateRelativeToRotation(new Vector3f(amt, 0, 0));
    }

    public void moveUp(float amt) {
        translateRelativeToRotation(new Vector3f(0, amt, 0));
    }

    public void rotate(Vector3f axis, float degrees) {
        rotation.rotateAxis((float)Math.toRadians(degrees), axis);
        setUpdateFlags(true);
    }

    public void rotateX(float degrees) {
        Vector3f localRotationAxis = new Vector3f(1, 0, 0);
        rotate(localRotationAxis, degrees);
    }

    public void rotateY(float degrees, boolean maintainWorldUpVector) {
        Vector3f localRotationAxis = new Vector3f(0, 1, 0);

        if (maintainWorldUpVector) {
            Quaternionf inverseRotation = new Quaternionf();
            rotation.invert(inverseRotation);

            localRotationAxis.rotate(inverseRotation);
        }

        rotate(localRotationAxis, degrees);
    }

    public void rotateZ(float degrees) {
        Vector3f localRotationAxis = new Vector3f(0, 0, 1);
        rotate(localRotationAxis, degrees);
    }

    public void scale(Vector3f v) {
        scale.x *= v.x;
        scale.y *= v.y;
        scale.z *= v.z;
        setUpdateFlags(true);
    }

    public void scale(float f) {
        scale(new Vector3f(f, f, f));
    }

    public Matrix4f getLocalToWorld() {
        if (localToWorldNeedsUpdate) {
            updateLocalToWorld();
        }
        return localToWorld;
    }

    private void updateLocalToWorld() {
        /*
            Because we want to do the following in this order:
                1. scale the object
                2. rotate it to the correct orientation
                3. translate it to it's correct position
            And because we are using column major matrices (and they are post-multiplied together), we must apply these
            transformations to an identity matrix in reverse order to get the desired effect.
         */

        if (parent != null) {
            localToWorld = new Matrix4f(parent.getLocalToWorld());
        } else {
            localToWorld = new Matrix4f();
        }

        localToWorld.translate(position);
        localToWorld.rotate(rotation);
        localToWorld.scale(scale);
        localToWorldNeedsUpdate = false;
    }

    public Matrix4f getWorldToLocal() {
        if (worldToLocalNeedsUpdate) {
            updateWorldToLocal();
        }
        return worldToLocal;
    }

    private void updateWorldToLocal() {
        getLocalToWorld().invert(worldToLocal);
        worldToLocalNeedsUpdate = false;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getWorldPosition() {
        getLocalToWorld();

        Vector3f pos = new Vector3f();
        localToWorld.getTranslation(pos);
        return pos;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Object3d getParent() {
        return parent;
    }

    public ArrayList<Object3d> getChildren() {
        return children;
    }

    public void destroy() {
        position = null;
        rotation = null;
        scale = null;

        localToWorld = null;
        worldToLocal = null;

        parent = null;
        children = null;
    }
}
