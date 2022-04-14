package dev.menace.module.modules.player;

import java.util.Arrays;
import java.util.List;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventLook;
import dev.menace.event.events.EventPlace;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.utils.entity.self.Rotations;
import dev.menace.utils.misc.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {
	
    public float[] serverRotations;

    private BlockData blockData;
    public static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button, Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon);
    private boolean isNeeded;
    public int currentSlot;
    
    //Settings
    BoolSetting sprint;
    BoolSetting silent;
    BoolSetting slowOnPlace;
    DoubleSetting slowFactor;
    
	public Scaffold() {
		super("Scaffold", 0, Category.PLAYER);
	}
	
	@Override
	public void setup() {
		sprint = new BoolSetting("Sprint", this, false);
		silent = new BoolSetting("Silent", this, false);
		slowOnPlace = new BoolSetting("SlowerOnPlace", this, false);
		slowFactor = new DoubleSetting("SlowFactor", this, 8, 1, 8);
		this.rSetting(sprint);
		this.rSetting(silent);
		this.rSetting(slowOnPlace);
		this.rSetting(slowFactor);
	}
	
	@Override
	public void onEnable() {
		if (MC.thePlayer == null) {
			this.toggle();
			return;
		}
		currentSlot = MC.thePlayer.inventory.currentItem;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if (MC.thePlayer == null) {
			return;
		}
		MC.thePlayer.inventory.currentItem = currentSlot;
		super.onDisable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
        MC.thePlayer.setSprinting(sprint.isChecked());

        this.blockData = this.getBlockData(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.0, MC.thePlayer.posZ));

        if (this.blockData == null) {
            this.blockData = this.getBlockData(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.0, MC.thePlayer.posZ).offset(EnumFacing.DOWN));
        }
        assert blockData != null;
        final float[] rotations = faceBlock(blockData.getPosition(), MC.theWorld.getBlockState(blockData.getPosition()).getBlock().getBlockBoundsMaxY() - MC.theWorld.getBlockState(blockData.getPosition()).getBlock().getBlockBoundsMinY() + 0.5D, true, true, true, true, 120);

        isNeeded = (MC.objectMouseOver == null || (MC.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK));


        serverRotations = rotations;
	}
	
	@EventTarget
	public void onPlace(EventPlace event) {
		 if (silent.isChecked()) {
	            currentSlot = MC.thePlayer.inventory.currentItem;
	        }

	        int slot = getBlockSlot();


	        if (slot == -1) {
	            return;

	        }

	        MC.thePlayer.inventory.currentItem = slot;

	        if (silent.isChecked()) {
	            MC.thePlayer.inventory.currentItem = currentSlot;
	        }


	        if (silent.isChecked()) {
	            currentSlot = MC.thePlayer.inventory.currentItem;

	            MC.thePlayer.inventory.currentItem = slot;

	            if (silent.isChecked()) {
	                MC.thePlayer.inventory.currentItem = currentSlot;
	            }
	        }

	        EnumFacing playerFace = blockData.getFacing();


	        if (MC.objectMouseOver.sideHit == playerFace) {

	            MC.thePlayer.fakeSwingItem();
	            MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, MC.thePlayer.getHeldItem(), blockData.getPosition(), MC.objectMouseOver.sideHit, MC.objectMouseOver.hitVec);

	            if (slowOnPlace.isChecked()) {
	                MC.thePlayer.motionX *= (slowFactor.getValue()/10);
	                MC.thePlayer.motionZ *= (slowFactor.getValue()/10);
	            }


	        }
	}
	
    @EventTarget
    public void onLook(EventLook e) {
       // e.setRotations(new float[]{Rotations.scaffoldYaw, Rotations.scaffoldPitch});
    }
	
    private BlockData getBlockData(BlockPos pos) {
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), Keyboard.isKeyDown(42) && MC.thePlayer.onGround && MC.thePlayer.fallDistance == 0.0f && MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.0, MC.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.MC.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), Keyboard.isKeyDown(42) && MC.thePlayer.onGround && MC.thePlayer.fallDistance == 0.0f && MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.0, MC.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.MC.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), Keyboard.isKeyDown(42) && MC.thePlayer.onGround && MC.thePlayer.fallDistance == 0.0f && MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.0, MC.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), Keyboard.isKeyDown(42) && MC.thePlayer.onGround && MC.thePlayer.fallDistance == 0.0f && MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.0, MC.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.NORTH, this.blockData);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(MC.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        return null;
    }
    
    private static class BlockData {
        public static BlockPos position;
        public static EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face, BlockData blockData) {
            BlockData.position = position;
            BlockData.face = face;
        }

        private BlockPos getPosition() {
            return position;
        }

        private EnumFacing getFacing() {
            return face;
        }
    }
    
    public float[] faceBlock(BlockPos pos, double yTranslation, boolean prediction, boolean randomAim, boolean randomizePitch, boolean clampYaw, float speed) {
        double x = (pos.getX() + (randomAim ? MathUtils.getRandom(0.45D, 0.5D) : 0.5D)) - MC.thePlayer.posX - (prediction ? MC.thePlayer.motionX : 0);
        double y = (pos.getY() - yTranslation) - (MC.thePlayer.posY + MC.thePlayer.getEyeHeight());
        double z = (pos.getZ() + (randomAim ? MathUtils.getRandom(0.45D, 0.5D) : 0.5D)) - MC.thePlayer.posZ - (prediction ? MC.thePlayer.motionZ : 0);

        if (randomizePitch) {
            y += MathUtils.getRandom(-0.05, 0.05);
        }
        double angle = MathHelper.sqrt_double(x * x + z * z);
        float yawAngle = (float) (MathHelper.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitchAngle = (float) -(MathHelper.atan2(y, angle) * 180.0D / Math.PI);

        float yaw = clampYaw ? yawAngle : updateRotation(MC.thePlayer.prevRotationYaw, yawAngle, speed);
        float pitch = updateRotation(MC.thePlayer.prevRotationPitch, pitchAngle, speed);
        final float sense = MC.gameSettings.mouseSensitivity;

        final float gcd = sense * 0.6f + 0.2f;
        float sense1 = gcd * gcd * gcd * 1.2f;

        yaw -= yaw % sense1;
        pitch -= pitch % sense1;

        return new float[]{yaw, MathHelper.clamp_float(pitch, -90, 90)};
    }


    float updateRotation(float currentRotation, float nextRotation, float rotationSpeed) {
        float f = MathHelper.wrapAngleTo180_float(nextRotation - currentRotation);
        if (f > rotationSpeed) {
            f = rotationSpeed;
        }
        if (f < -rotationSpeed) {
            f = -rotationSpeed;
        }
        return currentRotation + f;
    }
    
    private int getBlockSlot() {
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = MC.thePlayer.inventory.mainInventory[k];
            if (itemStack != null && isValid(itemStack) && itemStack.stackSize >= 1) {
                return k;
            }
        }
        return -1;
    }


    private boolean isValid(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock) {
            ItemBlock block = (ItemBlock) itemStack.getItem();
            return !blacklistedBlocks.contains(block.getBlock());
        }
        return false;
    }
}
