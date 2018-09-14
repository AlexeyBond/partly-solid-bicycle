package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.manager;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceTypeManager;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 * Maps resources loaded by {@link ResourceTypeManager resource type managers} to
 * {@link LogicNode logical tree nodes} with attached {@link ResourceReference
 * resource references}.
 * <pre>
 * /resources
 * /resources/textures
 * /resources/textures/sprites.spaceship <- {@link ResourceReference}{@literal <}{@link TextureRegion}{@literal >}
 * ...
 * /resources/sounds
 * /resources/sounds/spaceship.left-engine <- {@link ResourceReference}{@literal <}{@link Sound}{@literal >}
 * ...
 * </pre>
 */
@Component(
        name = "resourceManager",
        kind = "applicationComponent"
)
public class ResourceManager extends GroupNode {
    /**
     * Path to group node containing nodes with attached {@link ResourceTypeManager}s
     * for specific resource types.
     * <p>
     * Resources available as children of group nodes (children of the resource manager) are resolved
     * using a {@link ResourceTypeManager} attached to the child of node pointed by this path
     * with the same identifier as identifier of that group.
     * </p>
     */
    @SerializedName("typeManagersRoot")
    public LogicNodePath managersPath;

    @NotNull
    private final TypeManagerNodeResolver resolver;

    public ResourceManager() {
        this(new TypeManagerNodeResolver());
    }

    private ResourceManager(@NotNull TypeManagerNodeResolver resolver) {
        super(resolver, NullPopulator.INSTANCE);

        this.resolver = resolver;
    }

    @Override
    protected void onConnected0(@NotNull LogicNode parent, Id<LogicNode> id) {
        resolver.setManagersRoot(managersPath.lookup(this));

        super.onConnected0(parent, id);
    }
}
