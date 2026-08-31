/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package fi.dy.masa.litematica.schematic.pickblock;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.interfaces.ISchematicPickBlockEventListener;
import fi.dy.masa.litematica.interfaces.ISchematicPickBlockEventManager;
import fi.dy.masa.litematica.interfaces.ISchematicPickBlockSlotHandler;
import fi.dy.masa.litematica.schematic.pickblock.SchematicPickBlockEventResult;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import org.jetbrains.annotations.ApiStatus;

public class SchematicPickBlockEventHandler
implements ISchematicPickBlockEventManager {
    private static final SchematicPickBlockEventHandler INSTANCE = new SchematicPickBlockEventHandler();
    private final List<ISchematicPickBlockEventListener> handlers = new ArrayList<ISchematicPickBlockEventListener>();
    private class_1799 pickStack = class_1799.field_8037;
    private ISchematicPickBlockSlotHandler slotHandler = null;
    private boolean processingCancelled = false;
    private Supplier<String> processingCanceledBy = () -> "not_cancelled";

    public static SchematicPickBlockEventHandler getInstance() {
        return INSTANCE;
    }

    private SchematicPickBlockEventHandler() {
    }

    @Override
    public void registerSchematicPickBlockEventListener(@Nonnull ISchematicPickBlockEventListener listener) {
        if (!this.handlers.contains(listener)) {
            this.handlers.add(listener);
        }
    }

    @Override
    public SchematicPickBlockEventResult invokeRedirectPickBlockStack(ISchematicPickBlockEventListener listener, class_1799 newStack) {
        if (!this.pickStack.method_7960()) {
            return SchematicPickBlockEventResult.ERROR;
        }
        this.pickStack = newStack.method_7972();
        return SchematicPickBlockEventResult.SUCCESS;
    }

    @Override
    public SchematicPickBlockEventResult invokeRedirectPickBlockSlotHandler(ISchematicPickBlockEventListener listener, ISchematicPickBlockSlotHandler slotHandler) {
        if (this.slotHandler != null) {
            return SchematicPickBlockEventResult.ERROR;
        }
        this.slotHandler = slotHandler;
        return SchematicPickBlockEventResult.SUCCESS;
    }

    public boolean hasPickStack() {
        return !this.pickStack.method_7960();
    }

    public boolean hasSlotHandler() {
        return this.slotHandler != null;
    }

    public boolean isProcessingCancelled() {
        return this.processingCancelled;
    }

    public class_1799 getPickStack() {
        return this.pickStack;
    }

    public Supplier<String> getProcessingCanceledBy() {
        return this.processingCanceledBy;
    }

    @ApiStatus.Internal
    public boolean executePickBlockHandler(class_1937 world, class_2338 pos, class_1799 stack) {
        if (this.slotHandler != null) {
            SchematicPickBlockEventResult result = this.slotHandler.executePickBlock(world, pos, stack);
            if (result == SchematicPickBlockEventResult.CANCEL) {
                Litematica.LOGGER.warn("SchematicPickBlockEventHandler: Processing cancelled by: {} during 'executePickBlockHandler'", (Object)this.slotHandler.getName().get());
                this.processingCancelled = true;
                this.processingCanceledBy = this.slotHandler.getName();
                return true;
            }
            if (result == SchematicPickBlockEventResult.ERROR) {
                Litematica.LOGGER.error("SchematicPickBlockEventHandler: Error processing 'executePickBlockHandler' from: {}", (Object)this.slotHandler.getName().get());
            }
        }
        return false;
    }

    @ApiStatus.Internal
    public boolean onSchematicPickBlockStart(boolean closest) {
        for (ISchematicPickBlockEventListener handler : this.handlers) {
            if (this.isProcessingCancelled()) {
                handler.onSchematicPickBlockCancelled(this.getProcessingCanceledBy());
                continue;
            }
            SchematicPickBlockEventResult result = handler.onSchematicPickBlockStart(closest);
            if (result == SchematicPickBlockEventResult.CANCEL) {
                Litematica.LOGGER.warn("SchematicPickBlockEventHandler: Processing cancelled by: {} during 'onSchematicPickBlockStart'", (Object)handler.getName().get());
                this.processingCancelled = true;
                this.processingCanceledBy = handler.getName();
                continue;
            }
            if (result != SchematicPickBlockEventResult.ERROR) continue;
            Litematica.LOGGER.error("SchematicPickBlockEventHandler: Error processing 'onSchematicPickBlockStart' from: {}", (Object)handler.getName().get());
        }
        return this.isProcessingCancelled();
    }

    @ApiStatus.Internal
    public boolean onSchematicPickBlockPreGather(class_1937 schematicWorld, class_2338 pos, class_2680 expectedState) {
        for (ISchematicPickBlockEventListener handler : this.handlers) {
            if (this.isProcessingCancelled()) {
                handler.onSchematicPickBlockCancelled(this.getProcessingCanceledBy());
                continue;
            }
            SchematicPickBlockEventResult result = handler.onSchematicPickBlockPreGather(schematicWorld, pos, expectedState);
            if (result == SchematicPickBlockEventResult.CANCEL) {
                Litematica.LOGGER.warn("SchematicPickBlockEventHandler: Processing cancelled by: {} during 'onSchematicPickBlockPreGather'", (Object)handler.getName().get());
                this.processingCancelled = true;
                this.processingCanceledBy = handler.getName();
                continue;
            }
            if (result != SchematicPickBlockEventResult.ERROR) continue;
            Litematica.LOGGER.error("SchematicPickBlockEventHandler: Error processing 'onSchematicPickBlockPreGather' from: {}", (Object)handler.getName().get());
        }
        return this.isProcessingCancelled();
    }

    @ApiStatus.Internal
    public boolean onSchematicPickBlockPrePick(class_1937 schematicWorld, class_2338 pos, class_2680 expectedState, class_1799 stack) {
        for (ISchematicPickBlockEventListener handler : this.handlers) {
            if (this.isProcessingCancelled()) {
                handler.onSchematicPickBlockCancelled(this.getProcessingCanceledBy());
                continue;
            }
            SchematicPickBlockEventResult result = handler.onSchematicPickBlockPrePick(schematicWorld, pos, expectedState, stack);
            if (result == SchematicPickBlockEventResult.CANCEL) {
                Litematica.LOGGER.warn("SchematicPickBlockEventHandler: Processing cancelled by: {} during 'onSchematicPickBlockPrePick'", (Object)handler.getName().get());
                this.processingCancelled = true;
                this.processingCanceledBy = handler.getName();
                continue;
            }
            if (result != SchematicPickBlockEventResult.ERROR) continue;
            Litematica.LOGGER.error("SchematicPickBlockEventHandler: Error processing 'onSchematicPickBlockPrePick' from: {}", (Object)handler.getName().get());
        }
        return this.isProcessingCancelled();
    }

    @ApiStatus.Internal
    public void onSchematicPickBlockSuccess() {
        for (ISchematicPickBlockEventListener handler : this.handlers) {
            if (this.isProcessingCancelled()) {
                handler.onSchematicPickBlockCancelled(this.getProcessingCanceledBy());
                continue;
            }
            handler.onSchematicPickBlockSuccess();
        }
    }
}

