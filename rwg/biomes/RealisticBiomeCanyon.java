package rwg.biomes;

import java.util.Random;

import rwg.deco.DecoCacti;
import rwg.util.CanyonColor;
import rwg.util.CliffCalculator;
import rwg.util.PerlinNoise;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenerator;

public class RealisticBiomeCanyon extends BiomeGenBase implements RealisticBiome
{
	public RealisticBiomeCanyon(int id)
	{
		super(id);
		waterColorMultiplier = 0x00FF62;
	}

	@Override
	public void rDecorate(World world, Random rand, int chunkX, int chunkY, PerlinNoise perlin, float strength) 
	{
		if(rand.nextInt((int)(1f / strength)) == 0)
		{
			int i1 = chunkX + rand.nextInt(16) + 8;
			int j1 = chunkY + rand.nextInt(16) + 8;
		    int k1 = world.getHeightValue(i1, j1);
			if(k1 < 70)
			{
		    	(new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0)).generate(world, rand, i1, k1, j1);
			}
		}
		
		for (int b1 = 0; b1 < 5f * strength; b1++)
		{
			int j6 = chunkX + rand.nextInt(16) + 8;
			int k10 = chunkY + rand.nextInt(16) + 8;
			int z52 = world.getHeightValue(j6, k10);

			WorldGenerator worldgenerator;
			worldgenerator = new WorldGenShrub(0, 0);
			worldgenerator.setScale(1.0D, 1.0D, 1.0D);
			worldgenerator.generate(world, rand, j6, z52, k10);
		}
		
		if(rand.nextInt((int)(3f / strength)) == 0) 
		{
			int i18 = chunkX + rand.nextInt(16) + 8;
			int i23 = chunkY + rand.nextInt(16) + 8;
			(new WorldGenReed()).generate(world, rand, i18, 60 + rand.nextInt(8), i23);
		}
		
		if(rand.nextInt((int)(28f / strength)) == 0)
		{
			int j16 = chunkX + rand.nextInt(16) + 8;
			int j18 = rand.nextInt(128);
			int j21 = chunkY + rand.nextInt(16) + 8;
			(new WorldGenPumpkin()).generate(world, rand, j16, j18, j21);
		}
		
		for(int i15 = 0; i15 < 5f * strength; i15++)
		{
			int i17 = chunkX + rand.nextInt(16) + 8;
			int i20 = rand.nextInt(160);
			int l22 = chunkY + rand.nextInt(16) + 8;
			(new WorldGenDeadBush(Blocks.deadbush)).generate(world, rand, i17, i20, l22);
		}
		
		for(int k18 = 0; k18 < 25f * strength; k18++)
		{
			int k21 = chunkX + rand.nextInt(16) + 8;
			int j23 = rand.nextInt(160);
			int k24 = chunkY + rand.nextInt(16) + 8;
			(new DecoCacti(true)).generate(world, rand, k21, j23, k24);
		}
	}

	@Override
	public float rNoise(PerlinNoise perlin, int x, int y, float ocean) 
	{
		float b = perlin.noise2(x / 160f, y / 160f) * 60f;
		b *= b / 40f;
		
		float sb = 0f;
		if(b > 0f)
		{
			sb = b;
			sb = sb < 0f ? 0f : sb > 7f ? 7f : sb;
			sb = perlin.noise2(x / 12f, y / 12f) * sb;
		}
		b += sb;

		float c1 = 0f;
		if(b > 2f)
		{
			c1 = b > 2.5f ? 0.5f : b - 2f;
			c1 *= 35;
		}

		float c2 = 0f;
		if(b > 6.5f)
		{
			c2 = b > 7f ? 0.5f : b - 6.5f;
			c2 *= 35;
		}

		float c3 = 0f;
		if(b > 14f)
		{
			c3 = b > 14.5f ? 0.5f : b - 14f;
			c3 *= 35;
		}
		
		float c4 = 0f;
		if(b > 19f)
		{
			c4 = b > 19.5f ? 0.5f : b - 19f;
			c4 *= 35;
		}
		
		float bn = 0f;
		if(b < 5f)
		{
			bn = 5f - b;
			for(int i = 0; i < 3; i++)
			{
				bn *= bn / 4.5f;
			}
		}

		if(b < 5f)
		{
			b += perlin.noise2(x / 13f, y / 13f) * (bn + 3f - b) * 0.4f;
		}
		
		b += c1 + c2 + c3 + c4 - bn;
		
		return 69f + b;
	}

	@Override
	public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, Random rand, PerlinNoise perlin, float[] noise) 
	{
		float c = CliffCalculator.calc(x, y, noise);
		boolean cliff = c > 1.3f ? true : false;
		
		for(int k = 255; k > -1; k--)
		{
			Block b = blocks[(y * 16 + x) * 256 + k];
            if(b == Blocks.air)
            {
            	depth = -1;
            }
            else if(b == Blocks.stone)
            {
            	depth++;

        		if(depth > -1 && depth < 12)
	        	{
	            	if(cliff)
	            	{
	        			blocks[(y * 16 + x) * 256 + k] = Blocks.stained_hardened_clay;
	        			metadata[(y * 16 + x) * 256 + k] = CanyonColor.getColorForHeight(k);
	            	}
	            	else
	            	{
	        			if(depth > 4)
	        			{
		        			blocks[(y * 16 + x) * 256 + k] = Blocks.stained_hardened_clay;
		        			metadata[(y * 16 + x) * 256 + k] = CanyonColor.getColorForHeight(k);
	        			}
	        			else if(k > 77)
	        			{
	        				if(rand.nextInt(5) == 0)
	        				{
		        				blocks[(y * 16 + x) * 256 + k] = Blocks.dirt;
	        				}
	        				else
	        				{
		        				blocks[(y * 16 + x) * 256 + k] = Blocks.sand;
		        				metadata[(y * 16 + x) * 256 + k] = 1;
	        				}
	        			}
	        			else if(k < 62)
	        			{
	        				blocks[(y * 16 + x) * 256 + k] = Blocks.dirt;
	        			}
	        			else if(k < 70)
	        			{
	        				if(depth == 0)
	        				{
		        				int r = k - 62;
		        				if(rand.nextInt(r + 1) == 0)
		        				{
			        				blocks[(y * 16 + x) * 256 + k] = Blocks.grass;
		        				}
		        				else if(rand.nextInt((int)(r / 2f) + 1) == 0)
		        				{
			        				blocks[(y * 16 + x) * 256 + k] = Blocks.dirt;
		        				}
		        				else
		        				{
			        				blocks[(y * 16 + x) * 256 + k] = Blocks.sand;
			        				metadata[(y * 16 + x) * 256 + k] = 1;
		        				}
	        				}
	        				else
	        				{
		        				blocks[(y * 16 + x) * 256 + k] = Blocks.sand;
		        				metadata[(y * 16 + x) * 256 + k] = 1;
	        				}
	        			}
	        			else
	        			{
	        				blocks[(y * 16 + x) * 256 + k] = Blocks.sand;
	        				metadata[(y * 16 + x) * 256 + k] = 1;
	        			}
	            	}
        		}
        		else if(k > 63)
        		{
        			blocks[(y * 16 + x) * 256 + k] = Blocks.stained_hardened_clay;
        			metadata[(y * 16 + x) * 256 + k] = CanyonColor.getColorForHeight(k);
        		}
            }
		}
	}

	@Override
	public float r3Dnoise(float z) 
	{
		return 0f;
	}
}
