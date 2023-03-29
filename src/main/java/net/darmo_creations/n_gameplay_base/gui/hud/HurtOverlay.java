package net.darmo_creations.n_gameplay_base.gui.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * HUD overlay that displays a red halo effect to indicate the player’s health status.
 * The lower the player’s health is, the redder the overlay gets.
 */
public class HurtOverlay implements HudRenderCallback {
  private static final Identifier OVERLAY = new Identifier("textures/misc/nausea.png");
  private static final float RED = 0.2f;
  private static final float GREEN = 0.01f;
  private static final float BLUE = 0.01f;

  @Override
  public void onHudRender(MatrixStack matrixStack, float tickDelta) {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client != null) {
      ClientPlayerEntity player = client.player;
      if (player != null && player.getHealth() < player.getMaxHealth()) {
        // Adapted from net.minecraft.client.render.GameRenderer.renderNausea(float)
        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        float damageProportion = 1 - player.getHealth() / player.getMaxHealth();
        double overlayScale = MathHelper.lerp(damageProportion, 2, 1);
        double overlayW = screenW * overlayScale;
        double overlayH = screenH * overlayScale;
        double overlayX = (screenW - overlayW) / 2;
        double overlayY = (screenH - overlayH) / 2;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        GlStateManager.SrcFactor srcFactor = GlStateManager.SrcFactor.ONE;
        GlStateManager.DstFactor dstFactor = GlStateManager.DstFactor.ONE;
        RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcFactor, dstFactor);
        RenderSystem.setShaderColor(RED, GREEN, BLUE, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, OVERLAY);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(overlayX, overlayY + overlayH, -90).texture(0, 1).next();
        bufferBuilder.vertex(overlayX + overlayW, overlayY + overlayH, -90).texture(1, 1).next();
        bufferBuilder.vertex(overlayX + overlayW, overlayY, -90).texture(1, 0).next();
        bufferBuilder.vertex(overlayX, overlayY, -90).texture(0, 0).next();
        tessellator.draw();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
      }
    }
  }
}
