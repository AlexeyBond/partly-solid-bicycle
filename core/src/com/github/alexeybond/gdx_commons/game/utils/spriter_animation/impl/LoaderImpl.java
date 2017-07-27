package com.github.alexeybond.gdx_commons.game.utils.spriter_animation.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.FileReference;
import com.brashmonkey.spriter.Loader;
import com.github.alexeybond.gdx_commons.ioc.IoC;

public class LoaderImpl extends Loader<Sprite> {
    private final String regionPrefix, regionSuffix;

    public LoaderImpl(Data data, String regionPrefix, String regionSuffix) {
        super(data);
        this.regionPrefix = regionPrefix;
        this.regionSuffix = regionSuffix;
    }

    @Override
    protected Sprite loadResource(FileReference ref) {
        String fileName = data.getFile(ref).name;

        // Root is expected to be a path to a directory containing animation file ending with '/'
        String regionName = root
                + regionPrefix
                + fileName.replaceAll("\\.[\\w]+$", "")
                + regionSuffix;

        TextureRegion region = IoC.resolve("get texture region", regionName);

        return new Sprite(region);
    }

    public String root() {
        return root;
    }

    public Data data() {
        return data;
    }
}
