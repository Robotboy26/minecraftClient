// Minecraft Tracers Render Module for Fabric 1.20.1

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;

public class TracersRenderModule {

    private final MinecraftClient minecraftClient;

    public TracersRenderModule(MinecraftClient minecraftClient) {
        this.minecraftClient = minecraftClient;
    }

    public void render(MatrixStack matrixStack, float tickDelta) {
        Camera camera = minecraftClient.gameRenderer.getCamera();
        Entity cameraEntity = camera.getFocusedEntity();
        if (cameraEntity == null) {
            return;
        }
        Vec3d cameraPos = camera.getPos();
        List<Entity> entities = minecraftClient.world.getEntities();
        List<Entity> activeEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isInRange(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(), 64)
                    && entity != cameraEntity && entity.isAlive() && entity.isInRange(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(), 64)) {
                activeEntities.add(entity);
            }
        }
        drawTracers(matrixStack, cameraEntity, activeEntities);
    }

    private void drawTracers(MatrixStack matrixStack, Entity cameraEntity, List<Entity> entities) {
        for (Entity entity : entities) {
            Vec3d topLeft = new Vec3d(0, entity.getHeight() + 0.2, 0);
            Vec3d topRight = new Vec3d(0, entity.getHeight() + 0.2, 0);
            Vec3d bottomLeft = new Vec3d(0, 0, 0);
            Vec3d bottomRight = new Vec3d(0, 0, 0);
            Box box = entity.getBoundingBox();
            if (entity.getBoundingBox().intersects(cameraEntity.getBoundingBox())) {
                continue;
            }

            Vec3d projectedTopLeft = project(matrixStack, topLeft);
            Vec3d projectedTopRight = project(matrixStack, topRight);
            Vec3d projectedBottomLeft = project(matrixStack, bottomLeft);
            Vec3d projectedBottomRight = project(matrixStack, bottomRight);

            matrixStack.push();
            Matrix4f matrix4f = matrixStack.peek().getModel();

            float red = 0.0f, green = 0.0f, blue = 0.0f;
            if (entity.isPlayer()) {
                red = 0.0f;
                green = 1.0f;
                blue = 0.0f;
            } else if (entity.isAnimal()) {
                red = 1.0f;
                green = 1.0f;
                blue = 0.0f;
            } else {
                red = 1.0f;
                green = 0.0f;
                blue = 0.0f;
            }

            minecraftClient.gameRenderer.drawLine(matrix4f, (float)projectedTopLeft.x, (float)projectedTopLeft.y, (float)projectedTopLeft.z, (float)projectedBottomLeft.x, (float)projectedBottomLeft.y, (float)projectedBottomLeft.z, red, green, blue, 1.0f);
            minecraftClient.gameRenderer.drawLine(matrix4f, (float)projectedTopRight.x, (float)projectedTopRight.y, (float)projectedTopRight.z, (float)projectedBottomRight.x, (float)projectedBottomRight.y, (float)projectedBottomRight.z, red, green, blue, 1.0f);
            minecraftClient.gameRenderer.drawLine(matrix4f, (float)projectedTopLeft.x, (float)projectedTopLeft.y, (float)projectedTopLeft.z, (float)projectedTopRight.x, (float)projectedTopRight.y, (float)projectedTopRight.z, red, green, blue, 1.0f);
            minecraftClient.gameRenderer.drawLine(matrix4f, (float)projectedBottomLeft.x, (float)projectedBottomLeft.y, (float)projectedBottomLeft.z, (float)projectedBottomRight.x, (float)projectedBottomRight.y, (float)projectedBottomRight.z, red, green, blue, 1.0f);
            minecraftClient.gameRenderer.drawLine(matrix4f, (float)projectedTopLeft.x, (float)projectedTopLeft.y, (float)projectedTopLeft.z, (float)projectedBottomRight.x, (float)projectedBottomRight.y, (float)projectedBottomRight.z, red, green, blue, 1.0f);
            minecraftClient.gameRenderer.drawLine(matrix4f, (float)projectedTopRight.x, (float)projectedTopRight.y, (float)projectedTopRight.z, (float)projectedBottomLeft.x, (float)projectedBottomLeft.y, (float)projectedBottomLeft.z, red, green, blue, 1.0f);

            matrixStack.pop();
        }
    }

    private Vec3d project(MatrixStack matrixStack, Vec3d vec) {
        Matrix4f model = matrixStack.peek().getModel();
        Vector3f vector = new Vector3f((float) vec.x, (float) vec.y, (float) vec.z);
        vector.transform(model);
        return new Vec3d(vector.getX(), vector.getY(), vector.getZ());
    }

}
