package rwg.biomes.realistic;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import rwg.biomes.base.BaseBiomes;
import rwg.deco.DecoBlob;
import rwg.deco.DecoFlowers;
import rwg.deco.DecoGrass;
import rwg.deco.DecoLog;
import rwg.deco.trees.DecoPineTree;
import rwg.deco.trees.DecoShrub;
import rwg.deco.trees.DecoSmallPine;
import rwg.deco.trees.DecoSmallSpruce;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.PerlinNoise;
import rwg.util.SnowheightCalculator;

public class RealisticBiomeTundraHills extends RealisticBiomeBase
{
	public RealisticBiomeTundraHills(int sub)
	{
		super(sub, BaseBiomes.baseColdPlains);
	}

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, PerlinNoise perlin, CellNoise cell, float strength)
    {
		for (int l = 0; l < 3f * strength; ++l)
		{
			int i1 = chunkX + rand.nextInt(16) + 8;
			int j1 = chunkY + rand.nextInt(16) + 8;
		    int k1 = world.getHeightValue(i1, j1);
			if(k1 < 85 && (k1 < 64 || rand.nextInt(5) == 0))
			{
		    	(new DecoBlob(Blocks.cobblestone, 0)).generate(world, rand, i1, k1, j1);
			}
		}
    		
    	if(rand.nextInt((int)(1f / strength)) == 0)
    	{
			int i1 = chunkX + rand.nextInt(16) + 8;
			int j1 = chunkY + rand.nextInt(16) + 8;
		    int k1 = world.getHeightValue(i1, j1);
		    if(rand.nextInt(10) == 0)
		    {
    		    (new DecoShrub(rand.nextInt(5) + 4, rand.nextInt(2), rand.nextInt(2))).generate(world, rand, i1, k1, j1);
		    }
		    else
		    {
		    	(new DecoShrub(rand.nextInt(4) + 1, rand.nextInt(2), rand.nextInt(2))).generate(world, rand, i1, k1, j1);
		    }
    	}
		
		if(rand.nextInt((int)(3f / strength)) == 0)
		{
			int j6 = chunkX + rand.nextInt(16) + 8;
			int k10 = chunkY + rand.nextInt(16) + 8;
			int z52 = world.getHeightValue(j6, k10);
			
			if(z52 < 69)
			{
				WorldGenerator worldgenerator = new DecoSmallPine(1 + rand.nextInt(2), 4 + rand.nextInt(4));
				worldgenerator.setScale(1.0D, 1.0D, 1.0D);
				worldgenerator.generate(world, rand, j6, z52, k10);
			}
		}
		
		for(int f23 = 0; f23 < 2f * strength; f23++)
		{
			int j15 = chunkX + rand.nextInt(16) + 8;
			int j17 = rand.nextInt(128);
			int j20 = chunkY + rand.nextInt(16) + 8;
			(new DecoFlowers(new int[]{0})).generate(world, rand, j15, j17, j20);
		}
    	
		for(int l14 = 0; l14 < 4f * strength; l14++)
		{
			int l19 = chunkX + rand.nextInt(16) + 8;
			int k22 = rand.nextInt(128);
			int j24 = chunkY + rand.nextInt(16) + 8;
			(new DecoGrass(Blocks.tallgrass, 1)).generate(world, rand, l19, k22, j24);
		}
    }

    @Override
    public float rNoise(PerlinNoise perlin, CellNoise cell, int x, int y, float ocean, float border)
    {
		float h = perlin.noise2(x / 300f, y / 300f) * 160f;
		h *= h / 32f;
		h = h > 200f ? 200f : h;
		
		h = 180f;

		if(h > 2f)
		{
			float d = (h - 2f) / 2f > 8f ? 8f : (h - 2f) / 2f;
			h += perlin.noise2(x / 25f, y / 25f) * d;
			h += perlin.noise2(x / 50f, y / 50f) * d * 0.5f;

			if(h > 60f)
			{
				float d2 = (h - 60f) / 1.5f > 35f ? 35f : (h - 60f) / 1.5f;
				h += cell.noise(x / 25D, y / 25D, 1D) * d2;
			}
		}
		
		h += perlin.noise2(x / 18f, y / 18f) * 4;
		h += perlin.noise2(x / 8f, y / 8f) * 2;
		
		return h + 67f;
    }

    @Override
	public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, Random rand, PerlinNoise perlin, CellNoise cell, float[] noise) 
    {
		float p = perlin.noise2(i / 8f, j / 8f) * 0.5f;
		float c = CliffCalculator.calc(x, y, noise);
		int cliff = 0;
		boolean gravel = false;
		
    	Block b;
		for(int k = 255; k > -1; k--)
		{
			b = blocks[(y * 16 + x) * 256 + k];
            if(b == Blocks.air)
            {
            	depth = -1;
            }
            else if(b == Blocks.stone)
            {
            	depth++;
            	
            	if(depth == 0)
            	{
            		if(k < 63)
            		{
            			gravel = true;
            		}
            		
        			if(c > 0.45f && c > 1.5f - ((k - 60f) / 65f) + p)
        			{
        				cliff = 1;
        			}
            		if(c > 1.5f)
        			{
        				cliff = 2;
        			}
        			if(c < 0.3f + ((k - 100f) / 50f) + p)
        			{
        				cliff = 3;
        			}
            		
            		if(cliff == 1)
            		{
            			blocks[(y * 16 + x) * 256 + k] = rand.nextInt(3) == 0 ? Blocks.cobblestone : Blocks.stone; 
            		}
            		else if(cliff == 2)
            		{
        				blocks[(y * 16 + x) * 256 + k] = Blocks.stained_hardened_clay; 
        				metadata[(y * 16 + x) * 256 + k] = 9; 
            		}
            		else if(cliff == 3)
            		{
	        			blocks[(y * 16 + x) * 256 + k] = Blocks.snow;
            		}
            		else if(perlin.noise2(i / 50f, j / 50f) + p * 0.6f > 0.24f)
        			{
            			blocks[(y * 16 + x) * 256 + k] = Blocks.dirt;
            			metadata[(y * 16 + x) * 256 + k] = 2;
        			}
            		else if(k < 63)
            		{
            			blocks[(y * 16 + x) * 256 + k] = Blocks.gravel;
            			gravel = true;
            		}
            		else
            		{
            			blocks[(y * 16 + x) * 256 + k] = Blocks.grass;
            		}
            	}
            	else if(depth < 6)
        		{
            		if(cliff == 1)
            		{
            			blocks[(y * 16 + x) * 256 + k] = Blocks.stone; 
            		}
            		else if(cliff == 2)
            		{
        				blocks[(y * 16 + x) * 256 + k] = Blocks.stained_hardened_clay; 
        				metadata[(y * 16 + x) * 256 + k] = 9; 
            		}
            		else if(cliff == 3)
            		{
	        			blocks[(y * 16 + x) * 256 + k] = Blocks.snow;
            		}
            		else if(gravel)
            		{
            			blocks[(y * 16 + x) * 256 + k] = Blocks.gravel;
            		}
            		else
            		{
            			blocks[(y * 16 + x) * 256 + k] = Blocks.dirt;
            		}
        		}
            }
		}
	}
}