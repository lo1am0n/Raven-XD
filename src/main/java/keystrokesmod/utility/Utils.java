package keystrokesmod.utility;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.*;

import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.client.Settings;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.module.impl.combat.AutoClicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Mouse;

public class Utils {
   private static final Random rand = new Random();
   public static final Minecraft mc = Minecraft.getMinecraft();
   public static HashSet<String> friends = new HashSet<>();

   public static void sendMessage(String txt) {
      if (nullCheck()) {
         String m = r("&7[&dR&7]&r " + txt);
         mc.thePlayer.addChatMessage(new ChatComponentText(m));
      }
   }

   public static float getCompleteHealth(final EntityPlayer entityPlayer) {
      return entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount();
   }

   public static String r(String txt) {
      return txt.replaceAll("&", "§");
   }

   public static void correctValue(SliderSetting c, SliderSetting d) {
      if (c.getInput() > d.getInput()) {
         double p = c.getInput();
         c.setValue(d.getInput());
         d.setValue(p);
      }
   }

   public static boolean isFriended(EntityPlayer entityPlayer) {
      return !friends.isEmpty() && friends.contains(entityPlayer.getName().toLowerCase());
   }

   public static double getRandomValue(SliderSetting a, SliderSetting b, Random r) {
      return a.getInput() == b.getInput() ? a.getInput() : a.getInput() + r.nextDouble() * (b.getInput() - a.getInput());
   }

   public static boolean nullCheck() {
      return mc.thePlayer != null && mc.theWorld != null;
   }

   public static boolean isHypixel() {
      return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
   }

   public static net.minecraft.util.Timer getTimer() {
      return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
   }

   public static float n() {
      return ae(mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveForward, mc.thePlayer.movementInput.moveStrafe);
   }

   public static void setMotion(final double n) {
      if (n == 0.0) {
         mc.thePlayer.motionZ = 0.0;
         mc.thePlayer.motionX = 0.0;
         return;
      }
      final float n3 = n();
      mc.thePlayer.motionX = -Math.sin(n3) * n;
      mc.thePlayer.motionZ = Math.cos(n3) * n;
   }

   public static void resetTimer() {
      try {
         getTimer().timerSpeed = 1.0F;
      } catch (NullPointerException var1) {
      }
   }

   public static Random rand() {
      return rand;
   }

   public static boolean isStrafing() {
      return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
   }

   public static void aim(Entity en, float ps, boolean pc) {
      if (en != null) {
         float[] t = gr(en);
         if (t != null) {
            float y = t[0];
            float p = t[1] + 4.0F + ps;
            if (pc) {
               mc.getNetHandler().addToSendQueue(new C05PacketPlayerLook(y, p, mc.thePlayer.onGround));
            } else {
               mc.thePlayer.rotationYaw = y;
               mc.thePlayer.rotationPitch = p;
            }
         }

      }
   }

   public static float[] gr(Entity q) {
      if (q == null) {
         return null;
      } else {
         double diffX = q.posX - mc.thePlayer.posX;
         double diffY;
         if (q instanceof EntityLivingBase) {
            EntityLivingBase en = (EntityLivingBase)q;
            diffY = en.posY + (double)en.getEyeHeight() * 0.9D - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
         } else {
            diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0D - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
         }

         double diffZ = q.posZ - mc.thePlayer.posZ;
         double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
         return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
      }
   }

   public static double n(Entity en) {
      return ((double)(mc.thePlayer.rotationYaw - m(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
   }

   public static float m(Entity ent) {
      double x = ent.posX - mc.thePlayer.posX;
      double z = ent.posZ - mc.thePlayer.posZ;
      double yaw = Math.atan2(x, z) * 57.2957795D;
      return (float)(yaw * -1.0D);
   }

   public static boolean fov(Entity entity, float fov) {
      fov = (float)((double)fov * 0.5D);
      double v = ((double)(mc.thePlayer.rotationYaw - m(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
      return v > 0.0D && v < (double)fov || (double)(-fov) < v && v < 0.0D;
   }

   public static void ss(double s, boolean m) {
      if (!m || isStrafing()) {
         mc.thePlayer.motionX = -Math.sin(gd()) * s;
         mc.thePlayer.motionZ = Math.cos(gd()) * s;
      }
   }

   public static void ss2(double s) {
      double forward = mc.thePlayer.movementInput.moveForward;
      double strafe = mc.thePlayer.movementInput.moveStrafe;
      float yaw = mc.thePlayer.rotationYaw;
      if (forward == 0.0D && strafe == 0.0D) {
         mc.thePlayer.motionX = 0.0D;
         mc.thePlayer.motionZ = 0.0D;
      } else {
         if (forward != 0.0D) {
            if (strafe > 0.0D) {
               yaw += (float)(forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
               yaw += (float)(forward > 0.0D ? 45 : -45);
            }

            strafe = 0.0D;
            if (forward > 0.0D) {
               forward = 1.0D;
            } else if (forward < 0.0D) {
               forward = -1.0D;
            }
         }

         double rad = Math.toRadians((double)(yaw + 90.0F));
         double sin = Math.sin(rad);
         double cos = Math.cos(rad);
         mc.thePlayer.motionX = forward * s * cos + strafe * s * sin;
         mc.thePlayer.motionZ = forward * s * sin - strafe * s * cos;
      }

   }

   public static float gd() {
      float yw = mc.thePlayer.rotationYaw;
      if (mc.thePlayer.moveForward < 0.0F) {
         yw += 180.0F;
      }

      float f;
      if (mc.thePlayer.moveForward < 0.0F) {
         f = -0.5F;
      } else if (mc.thePlayer.moveForward > 0.0F) {
         f = 0.5F;
      } else {
         f = 1.0F;
      }

      if (mc.thePlayer.moveStrafing > 0.0F) {
         yw -= 90.0F * f;
      }

      if (mc.thePlayer.moveStrafing < 0.0F) {
         yw += 90.0F * f;
      }

      yw *= 0.017453292F;
      return yw;
   }

   public static float ae(float n, final float n2, final float n3) {
      float n4 = 1.0f;
      if (n2 < 0.0f) {
         n += 180.0f;
         n4 = -0.5f;
      }
      else if (n2 > 0.0f) {
         n4 = 0.5f;
      }
      if (n3 > 0.0f) {
         n -= 90.0f * n4;
      }
      else if (n3 < 0.0f) {
         n += 90.0f * n4;
      }
      return n * 0.017453292f;
   }

   public static double gs() {
      return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
   }

   public static double gbps(Entity en, int d) {
      double x = en.posX - en.prevPosX;
      double z = en.posZ - en.prevPosZ;
      double sp = Math.sqrt(x * x + z * z) * 20.0D;
      return rnd(sp, d);
   }

   public static boolean ilc() {
      if (ModuleManager.autoClicker.isEnabled()) {
         return AutoClicker.leftClick.isToggled() && Mouse.isButtonDown(0);
      } else return CPSCalculator.f() > 1 && System.currentTimeMillis() - CPSCalculator.LL < 300L;
   }

   public static int getChroma(long speed, long... delay) {
      long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
      return Color.getHSBColor((float)(time % (15000L / speed)) / (15000.0F / (float)speed), 1.0F, 1.0F).getRGB();
   }

   public static double rnd(double n, int d) {
      if (d == 0) {
         return (double)Math.round(n);
      } else {
         double p = Math.pow(10.0D, (double)d);
         return (double)Math.round(n * p) / p;
      }
   }

   public static String str(String s) {
      char[] n = StringUtils.stripControlCodes(s).toCharArray();
      StringBuilder v = new StringBuilder();

      for (char c : n) {
         if (c < 127 && c > 20) {
            v.append(c);
         }
      }

      return v.toString();
   }

   public static List<String> gsl() {
      List<String> lines = new ArrayList();
      if (mc.theWorld == null) {
         return lines;
      } else {
         Scoreboard scoreboard = mc.theWorld.getScoreboard();
         if (scoreboard == null) {
            return lines;
         } else {
            ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
            if (objective == null) {
               return lines;
            } else {
               Collection<Score> scores = scoreboard.getSortedScores(objective);
               List<Score> list = new ArrayList();
               Iterator var5 = scores.iterator();

               Score score;
               while(var5.hasNext()) {
                  score = (Score)var5.next();
                  if (score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#")) {
                     list.add(score);
                  }
               }

               if (list.size() > 15) {
                  scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
               } else {
                  scores = list;
               }

               var5 = scores.iterator();

               while(var5.hasNext()) {
                  score = (Score)var5.next();
                  ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                  lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
               }

               return lines;
            }
         }
      }
   }

   public static void rsa() {
      EntityPlayerSP p = mc.thePlayer;
      int armSwingEnd = p.isPotionActive(Potion.digSpeed) ? 6 - (1 + p.getActivePotionEffect(Potion.digSpeed).getAmplifier()) : (p.isPotionActive(Potion.digSlowdown) ? 6 + (1 + p.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
      if (!p.isSwingInProgress || p.swingProgressInt >= armSwingEnd / 2 || p.swingProgressInt < 0) {
         p.swingProgressInt = -1;
         p.isSwingInProgress = true;
      }

   }

   public static String uf(String s) {
      return s.substring(0, 1).toUpperCase() + s.substring(1);
   }

   public static boolean overAir() {
      return mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ));
   }

   public static boolean holdingWeapon() {
      if (mc.thePlayer.getHeldItem() == null) {
         return false;
      }
      final Item getItem = mc.thePlayer.getHeldItem().getItem();
      return getItem instanceof ItemSword || (Settings.weaponAxe.isToggled() && getItem instanceof ItemAxe) || (Settings.weaponRod.isToggled() && getItem instanceof ItemFishingRod) || (Settings.weaponStick.isToggled() && getItem == Items.stick);
   }
}
