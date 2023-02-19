package net.optifine.entity.model;

import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;

public class ModelAdapterLeadKnot extends ModelAdapter
{
    public ModelAdapterLeadKnot()
    {
        super(EntityLeashKnot.class, "lead_knot", 0.0F);
    }

    public ModelBase makeModel()
    {
        return new ModelLeashKnot();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelLeashKnot))
        {
            return null;
        }
        else
        {
            ModelLeashKnot modelleashknot = (ModelLeashKnot)model;
            return modelPart.equals("knot") ? modelleashknot.field_110723_a : null;
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"knot"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderLeashKnot renderleashknot = new RenderLeashKnot(rendermanager);

        if (RenderLeashKnot.leashKnotModel == null)
        {
            Config.warn("Field not found: RenderLeashKnot.leashKnotModel");
            return null;
        }
        else
        {
            RenderLeashKnot.leashKnotModel = (ModelLeashKnot)modelBase;
            renderleashknot.shadowSize = shadowSize;
            return renderleashknot;
        }
    }
}
