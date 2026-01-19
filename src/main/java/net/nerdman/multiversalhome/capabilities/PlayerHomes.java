package net.nerdman.multiversalhome.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerHomes {
    private List<CompoundTag> homes = new ArrayList<>();

    public List<CompoundTag> getHomes(){
        return homes;
    }

    public int addHome(String name, BlockPos pos, String dimension){
        CompoundTag new_home = new CompoundTag();
        int[] position = {pos.getX(), pos.getY(), pos.getZ()};

        //check if a home with that name/position already exists
        //if it does, replace the position/name
        for(int i = 0; i < homes.size(); i++){
            if(name.equals(homes.get(i).getString("Name"))){
                homes.get(i).putIntArray("Pos", position);
                homes.get(i).putString("Dimension", dimension);
                return 2;
            } else if (Arrays.equals(position, homes.get(i).getIntArray("Pos")) && dimension.equals(homes.get(i).getString("Dimension"))){
                homes.get(i).putString("Name", name);
                return 3;
            }
        }

        //if it doesn't, then add the home
        new_home.putString("Name", name);
        new_home.putIntArray("Pos", position);
        new_home.putString("Dimension", dimension);
        homes.add(new_home);
        return 1;
    }

    public boolean removeHome(String name){
        for(int i = 0; i < homes.size(); i++){
            if(name.equals(homes.get(i).getString("Name"))){
                homes.remove(i);
                return true;
            }
        }
        return false;
    }

    public void copyFrom(PlayerHomes source){
        this.homes = source.homes;
    }

    public void saveNBTData(CompoundTag nbt){
        ListTag nbtTagList = new ListTag();
        for(int i = 0; i < homes.size(); i++){
            nbtTagList.add(homes.get(i));
        }
        nbt.put("Homes", nbtTagList);
        nbt.putInt("Size", homes.size());
    }

    public void loadNBTData(CompoundTag nbt){
        ListTag tagList = nbt.getList("Homes", Tag.TAG_COMPOUND);
        homes = new ArrayList<>();
        for(int i = 0; i < tagList.size(); i++){
            homes.add(tagList.getCompound(i));
        }
    }
}
