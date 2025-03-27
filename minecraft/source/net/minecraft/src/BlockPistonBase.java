package net.minecraft.src;

import java.util.ArrayList;

public class BlockPistonBase extends Block {
	private boolean isSticky;
	private boolean field_31048_b;

	public BlockPistonBase(int var1, int var2, boolean var3) {
		super(var1, var2, Material.field_31067_B);
		this.isSticky = var3;
		this.setStepSound(soundStoneFootstep);
		this.setHardness(0.5F);
	}

	public int func_31040_i() {
		return this.isSticky ? 106 : 107;
	}

	public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		int var3 = func_31044_d(var2);
		return var3 > 5 ? this.blockIndexInTexture : (var1 == var3 ? (!isPowered(var2) && this.minX <= 0.0D && this.minY <= 0.0D && this.minZ <= 0.0D && this.maxX >= 1.0D && this.maxY >= 1.0D && this.maxZ >= 1.0D ? this.blockIndexInTexture : 110) : (var1 == PistonBlockTextures.field_31057_a[var3] ? 109 : 108));
	}

	public int getRenderType() {
		return 16;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		return false;
	}

	public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5) {
		int var6 = func_31039_c(var1, var2, var3, var4, (EntityPlayer)var5);
		var1.setBlockMetadataWithNotify(var2, var3, var4, var6);
		if(!var1.multiplayerWorld) {
			this.func_31043_h(var1, var2, var3, var4);
		}

	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(!var1.multiplayerWorld && !this.field_31048_b) {
			this.func_31043_h(var1, var2, var3, var4);
		}

	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
		if(!var1.multiplayerWorld && var1.getBlockTileEntity(var2, var3, var4) == null) {
			this.func_31043_h(var1, var2, var3, var4);
		}

	}

	private void func_31043_h(World var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		int var6 = func_31044_d(var5);
		boolean var7 = this.func_31041_f(var1, var2, var3, var4, var6);
		if(var5 != 7) {
			if(var7 && !isPowered(var5)) {
				if(func_31045_h(var1, var2, var3, var4, var6)) {
					var1.setBlockMetadata(var2, var3, var4, var6 | 8);
					var1.playNoteAt(var2, var3, var4, 0, var6);
				}
			} else if(!var7 && isPowered(var5)) {
				var1.setBlockMetadata(var2, var3, var4, var6);
				var1.playNoteAt(var2, var3, var4, 1, var6);
			}

		}
	}

	private boolean func_31041_f(World var1, int var2, int var3, int var4, int var5) {
		return var5 != 0 && var1.isBlockIndirectlyProvidingPowerTo(var2, var3 - 1, var4, 0) ? true : (var5 != 1 && var1.isBlockIndirectlyProvidingPowerTo(var2, var3 + 1, var4, 1) ? true : (var5 != 2 && var1.isBlockIndirectlyProvidingPowerTo(var2, var3, var4 - 1, 2) ? true : (var5 != 3 && var1.isBlockIndirectlyProvidingPowerTo(var2, var3, var4 + 1, 3) ? true : (var5 != 5 && var1.isBlockIndirectlyProvidingPowerTo(var2 + 1, var3, var4, 5) ? true : (var5 != 4 && var1.isBlockIndirectlyProvidingPowerTo(var2 - 1, var3, var4, 4) ? true : (var1.isBlockIndirectlyProvidingPowerTo(var2, var3, var4, 0) ? true : (var1.isBlockIndirectlyProvidingPowerTo(var2, var3 + 2, var4, 1) ? true : (var1.isBlockIndirectlyProvidingPowerTo(var2, var3 + 1, var4 - 1, 2) ? true : (var1.isBlockIndirectlyProvidingPowerTo(var2, var3 + 1, var4 + 1, 3) ? true : (var1.isBlockIndirectlyProvidingPowerTo(var2 - 1, var3 + 1, var4, 4) ? true : var1.isBlockIndirectlyProvidingPowerTo(var2 + 1, var3 + 1, var4, 5)))))))))));
	}

	public void playBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
		this.field_31048_b = true;
		if(var5 == 0) {
			if(this.func_31047_i(var1, var2, var3, var4, var6)) {
				var1.setBlockMetadataWithNotify(var2, var3, var4, var6 | 8);
				var1.playSoundEffect((double)var2 + 0.5D, (double)var3 + 0.5D, (double)var4 + 0.5D, "tile.piston.out", 0.5F, var1.rand.nextFloat() * 0.25F + 0.6F);
			}
		} else if(var5 == 1) {
			TileEntity var8 = var1.getBlockTileEntity(var2 + PistonBlockTextures.field_31056_b[var6], var3 + PistonBlockTextures.field_31059_c[var6], var4 + PistonBlockTextures.field_31058_d[var6]);
			if(var8 != null && var8 instanceof TileEntityPiston) {
				((TileEntityPiston)var8).func_31011_l();
			}

			var1.setBlockAndMetadata(var2, var3, var4, Block.pistonMoving.blockID, var6);
			var1.setBlockTileEntity(var2, var3, var4, BlockPistonMoving.func_31036_a(this.blockID, var6, var6, false, true));
			if(this.isSticky) {
				int var9 = var2 + PistonBlockTextures.field_31056_b[var6] * 2;
				int var10 = var3 + PistonBlockTextures.field_31059_c[var6] * 2;
				int var11 = var4 + PistonBlockTextures.field_31058_d[var6] * 2;
				int var12 = var1.getBlockId(var9, var10, var11);
				int var13 = var1.getBlockMetadata(var9, var10, var11);
				boolean var14 = false;
				if(var12 == Block.pistonMoving.blockID) {
					TileEntity var15 = var1.getBlockTileEntity(var9, var10, var11);
					if(var15 != null && var15 instanceof TileEntityPiston) {
						TileEntityPiston var16 = (TileEntityPiston)var15;
						if(var16.func_31009_d() == var6 && var16.func_31015_b()) {
							var16.func_31011_l();
							var12 = var16.getStoredBlockID();
							var13 = var16.getBlockMetadata();
							var14 = true;
						}
					}
				}

				if(var14 || var12 <= 0 || !canPushBlock(var12, var1, var9, var10, var11, false) || Block.blocksList[var12].getMobilityFlag() != 0 && var12 != Block.pistonBase.blockID && var12 != Block.pistonStickyBase.blockID) {
					if(!var14) {
						this.field_31048_b = false;
						var1.setBlockWithNotify(var2 + PistonBlockTextures.field_31056_b[var6], var3 + PistonBlockTextures.field_31059_c[var6], var4 + PistonBlockTextures.field_31058_d[var6], 0);
						this.field_31048_b = true;
					}
				} else {
					this.field_31048_b = false;
					var1.setBlockWithNotify(var9, var10, var11, 0);
					this.field_31048_b = true;
					var2 += PistonBlockTextures.field_31056_b[var6];
					var3 += PistonBlockTextures.field_31059_c[var6];
					var4 += PistonBlockTextures.field_31058_d[var6];
					var1.setBlockAndMetadata(var2, var3, var4, Block.pistonMoving.blockID, var13);
					var1.setBlockTileEntity(var2, var3, var4, BlockPistonMoving.func_31036_a(var12, var13, var6, false, false));
				}
			} else {
				this.field_31048_b = false;
				var1.setBlockWithNotify(var2 + PistonBlockTextures.field_31056_b[var6], var3 + PistonBlockTextures.field_31059_c[var6], var4 + PistonBlockTextures.field_31058_d[var6], 0);
				this.field_31048_b = true;
			}

			var1.playSoundEffect((double)var2 + 0.5D, (double)var3 + 0.5D, (double)var4 + 0.5D, "tile.piston.in", 0.5F, var1.rand.nextFloat() * 0.15F + 0.6F);
		}

		this.field_31048_b = false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		if(isPowered(var5)) {
			switch(func_31044_d(var5)) {
			case 0:
				this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 12.0F / 16.0F, 1.0F);
				break;
			case 2:
				this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F / 16.0F);
				break;
			case 4:
				this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 12.0F / 16.0F, 1.0F, 1.0F);
			}
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, ArrayList var6) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public static int func_31044_d(int var0) {
		return var0 & 7;
	}

	public static boolean isPowered(int var0) {
		return (var0 & 8) != 0;
	}

	private static int func_31039_c(World var0, int var1, int var2, int var3, EntityPlayer var4) {
		if(MathHelper.abs((float)var4.posX - (float)var1) < 2.0F && MathHelper.abs((float)var4.posZ - (float)var3) < 2.0F) {
			double var5 = var4.posY + 1.82D - (double)var4.yOffset;
			if(var5 - (double)var2 > 2.0D) {
				return 1;
			}

			if((double)var2 - var5 > 0.0D) {
				return 0;
			}
		}

		int var7 = MathHelper.floor_double((double)(var4.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return var7 == 0 ? 2 : (var7 == 1 ? 5 : (var7 == 2 ? 3 : (var7 == 3 ? 4 : 0)));
	}

	private static boolean canPushBlock(int var0, World var1, int var2, int var3, int var4, boolean var5) {
		if(var0 == Block.obsidian.blockID) {
			return false;
		} else {
			if(var0 != Block.pistonBase.blockID && var0 != Block.pistonStickyBase.blockID) {
				if(Block.blocksList[var0].getHardness() == -1.0F) {
					return false;
				}

				if(Block.blocksList[var0].getMobilityFlag() == 2) {
					return false;
				}

				if(!var5 && Block.blocksList[var0].getMobilityFlag() == 1) {
					return false;
				}
			} else if(isPowered(var1.getBlockMetadata(var2, var3, var4))) {
				return false;
			}

			TileEntity var6 = var1.getBlockTileEntity(var2, var3, var4);
			return var6 == null;
		}
	}

	private static boolean func_31045_h(World var0, int var1, int var2, int var3, int var4) {
		int var5 = var1 + PistonBlockTextures.field_31056_b[var4];
		int var6 = var2 + PistonBlockTextures.field_31059_c[var4];
		int var7 = var3 + PistonBlockTextures.field_31058_d[var4];
		int var8 = 0;

		while(true) {
			if(var8 < 13) {
				if(var6 <= 0 || var6 >= 127) {
					return false;
				}

				int var9 = var0.getBlockId(var5, var6, var7);
				if(var9 != 0) {
					if(!canPushBlock(var9, var0, var5, var6, var7, true)) {
						return false;
					}

					if(Block.blocksList[var9].getMobilityFlag() != 1) {
						if(var8 == 12) {
							return false;
						}

						var5 += PistonBlockTextures.field_31056_b[var4];
						var6 += PistonBlockTextures.field_31059_c[var4];
						var7 += PistonBlockTextures.field_31058_d[var4];
						++var8;
						continue;
					}
				}
			}

			return true;
		}
	}

	private boolean func_31047_i(World var1, int var2, int var3, int var4, int var5) {
		int var6 = var2 + PistonBlockTextures.field_31056_b[var5];
		int var7 = var3 + PistonBlockTextures.field_31059_c[var5];
		int var8 = var4 + PistonBlockTextures.field_31058_d[var5];
		int var9 = 0;

		while(true) {
			int var10;
			if(var9 < 13) {
				if(var7 <= 0 || var7 >= 127) {
					return false;
				}

				var10 = var1.getBlockId(var6, var7, var8);
				if(var10 != 0) {
					if(!canPushBlock(var10, var1, var6, var7, var8, true)) {
						return false;
					}

					if(Block.blocksList[var10].getMobilityFlag() != 1) {
						if(var9 == 12) {
							return false;
						}

						var6 += PistonBlockTextures.field_31056_b[var5];
						var7 += PistonBlockTextures.field_31059_c[var5];
						var8 += PistonBlockTextures.field_31058_d[var5];
						++var9;
						continue;
					}

					Block.blocksList[var10].dropBlockAsItem(var1, var6, var7, var8, var1.getBlockMetadata(var6, var7, var8));
					var1.setBlockWithNotify(var6, var7, var8, 0);
				}
			}

			while(var6 != var2 || var7 != var3 || var8 != var4) {
				var9 = var6 - PistonBlockTextures.field_31056_b[var5];
				var10 = var7 - PistonBlockTextures.field_31059_c[var5];
				int var11 = var8 - PistonBlockTextures.field_31058_d[var5];
				int var12 = var1.getBlockId(var9, var10, var11);
				int var13 = var1.getBlockMetadata(var9, var10, var11);
				if(var12 == this.blockID && var9 == var2 && var10 == var3 && var11 == var4) {
					var1.setBlockAndMetadata(var6, var7, var8, Block.pistonMoving.blockID, var5 | (this.isSticky ? 8 : 0));
					var1.setBlockTileEntity(var6, var7, var8, BlockPistonMoving.func_31036_a(Block.pistonExtension.blockID, var5 | (this.isSticky ? 8 : 0), var5, true, false));
				} else {
					var1.setBlockAndMetadata(var6, var7, var8, Block.pistonMoving.blockID, var13);
					var1.setBlockTileEntity(var6, var7, var8, BlockPistonMoving.func_31036_a(var12, var13, var5, true, false));
				}

				var6 = var9;
				var7 = var10;
				var8 = var11;
			}

			return true;
		}
	}
}
