package keystrokesmod.clickgui.components.impl;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.setting.impl.SliderSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class SliderComponent extends Component {
   private SliderSetting sliderSetting;
   private ModuleComponent moduleComponent;
   private int o;
   private int x;
   private int y;
   private boolean d = false;
   private double w;

   public SliderComponent(SliderSetting sliderSetting, ModuleComponent moduleComponent, int o) {
      this.sliderSetting = sliderSetting;
      this.moduleComponent = moduleComponent;
      this.x = moduleComponent.categoryComponent.getX() + moduleComponent.categoryComponent.gw();
      this.y = moduleComponent.categoryComponent.getY() + moduleComponent.o;
      this.o = o;
   }

   public void render() {
      net.minecraft.client.gui.Gui.drawRect(this.moduleComponent.categoryComponent.getX() + 4, this.moduleComponent.categoryComponent.getY() + this.o + 11, this.moduleComponent.categoryComponent.getX() + 4 + this.moduleComponent.categoryComponent.gw() - 8, this.moduleComponent.categoryComponent.getY() + this.o + 15, -12302777);
      int l = this.moduleComponent.categoryComponent.getX() + 4;
      int r = this.moduleComponent.categoryComponent.getX() + 4 + (int)this.w;
      if (r - l > 84) {
         r = l + 84;
      }

      net.minecraft.client.gui.Gui.drawRect(l, this.moduleComponent.categoryComponent.getY() + this.o + 11, r, this.moduleComponent.categoryComponent.getY() + this.o + 15, Color.getHSBColor((float)(System.currentTimeMillis() % 11000L) / 11000.0F, 0.75F, 0.9F).getRGB());
      GL11.glPushMatrix();
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      String value;
      if (this.sliderSetting.isString) {
         value = this.sliderSetting.getOptions()[(int) this.sliderSetting.getInput()];
      }
      else {
         value = String.valueOf(this.sliderSetting.getInput());
      }
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.sliderSetting.getName() + ": " + value, (float)((int)((float)(this.moduleComponent.categoryComponent.getX() + 4) * 2.0F)), (float)((int)((float)(this.moduleComponent.categoryComponent.getY() + this.o + 3) * 2.0F)), -1);
      GL11.glPopMatrix();
   }

   public void so(int n) {
      this.o = n;
   }

   public void drawScreen(int x, int y) {
      this.y = this.moduleComponent.categoryComponent.getY() + this.o;
      this.x = this.moduleComponent.categoryComponent.getX();
      double d = Math.min(this.moduleComponent.categoryComponent.gw() - 8, Math.max(0, x - this.x));
      this.w = (double)(this.moduleComponent.categoryComponent.gw() - 8) * (this.sliderSetting.getInput() - this.sliderSetting.getMin()) / (this.sliderSetting.getMax() - this.sliderSetting.getMin());
      if (this.d) {
         if (d == 0.0D) {
            this.sliderSetting.setValue(this.sliderSetting.getMin());
         } else {
            double n = roundToInterval(d / (double)(this.moduleComponent.categoryComponent.gw() - 8) * (this.sliderSetting.getMax() - this.sliderSetting.getMin()) + this.sliderSetting.getMin(), 2);
            this.sliderSetting.setValue(n);
         }
      }

   }

   private static double roundToInterval(double v, int p) {
      if (p < 0) {
         return 0.0D;
      } else {
         BigDecimal bd = new BigDecimal(v);
         bd = bd.setScale(p, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public void onClick(int x, int y, int b) {
      if (this.u(x, y) && b == 0 && this.moduleComponent.po) {
         this.d = true;
      }

      if (this.i(x, y) && b == 0 && this.moduleComponent.po) {
         this.d = true;
      }

   }

   public void mouseReleased(int x, int y, int m) {
      this.d = false;
   }

   public boolean u(int x, int y) {
      return x > this.x && x < this.x + this.moduleComponent.categoryComponent.gw() / 2 + 1 && y > this.y && y < this.y + 16;
   }

   public boolean i(int x, int y) {
      return x > this.x + this.moduleComponent.categoryComponent.gw() / 2 && x < this.x + this.moduleComponent.categoryComponent.gw() && y > this.y && y < this.y + 16;
   }
}
