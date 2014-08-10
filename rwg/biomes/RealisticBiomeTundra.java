package rwg.biomes;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import rwg.deco.DecoBlob;
import rwg.deco.DecoFlowers;
import rwg.deco.DecoGrass;
import rwg.deco.trees.DecoPineTree;
import rwg.deco.trees.DecoRedWood;
import rwg.util.CliffCalculator;
import rwg.util.PerlinNoise;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenerator;

public class RealisticBiomeTundra extends BiomeGenBase implements RealisticBiome
{
	public RealisticBiomeTundra(int id) 
	{
		super(id);
	}

	@Override
	public void rDecorate(World world, Random rand, int chunkX, int chunkY, PerlinNoise perlin, float strength) 
	{
	}

	@Override
	public float rNoise(PerlinNoise perlin, int x, int y, float ocean) 
	{
		return 70f + perlin.noise2(x / 140f, y / 140f) * 25;
	}

	@Override
	public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, Random rand, PerlinNoise perlin, float[] noise) 
	{
		boolean gravel = false;
		
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

        		if(depth == 0)
        		{
        			if(k < 62)
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
        			if(gravel)
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

	@Override
	public float r3Dnoise(float z)
	{
		return 0;
	}
	
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int i, int dont, int care)
    {
        return ColorizerGrass.getGrassColor(0.7f, 0.5f);
    }

    @SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int i, int dont, int care)
    {
        return ColorizerFoliage.getFoliageColor(0.7f, 0.5f);
    }
}
