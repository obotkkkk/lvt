/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetListEntrySortable
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.PositionUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1059
 *  net.minecraft.class_1087
 *  net.minecraft.class_1799
 *  net.minecraft.class_1921
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2350
 *  net.minecraft.class_2464
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_308
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  net.minecraft.class_4587$class_4665
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_4608
 *  net.minecraft.class_5819
 *  net.minecraft.class_6575
 *  net.minecraft.class_765
 *  net.minecraft.class_776
 *  net.minecraft.class_777
 *  net.minecraft.class_7923
 *  net.minecraft.class_9799
 *  org.joml.Quaternionf
 */
package fi.dy.masa.litematica.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.gui.GuiSchematicVerifier;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.gui.widgets.WidgetListSchematicVerificationResults;
import fi.dy.masa.litematica.render.RenderUtils;
import fi.dy.masa.litematica.schematic.verifier.SchematicVerifier;
import fi.dy.masa.litematica.util.ItemUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntrySortable;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1059;
import net.minecraft.class_1087;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2350;
import net.minecraft.class_2464;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5819;
import net.minecraft.class_6575;
import net.minecraft.class_765;
import net.minecraft.class_776;
import net.minecraft.class_777;
import net.minecraft.class_7923;
import net.minecraft.class_9799;
import org.joml.Quaternionf;

public class WidgetSchematicVerificationResult
extends WidgetListEntrySortable<GuiSchematicVerifier.BlockMismatchEntry> {
    private static final class_6575 RAND = new class_6575(0L);
    public static final String HEADER_EXPECTED = "litematica.gui.label.schematic_verifier.expected";
    public static final String HEADER_FOUND = "litematica.gui.label.schematic_verifier.found";
    public static final String HEADER_COUNT = "litematica.gui.label.schematic_verifier.count";
    private static int maxNameLengthExpected;
    private static int maxNameLengthFound;
    private static int maxCountLength;
    private final class_776 blockModelShapes;
    private final GuiSchematicVerifier guiSchematicVerifier;
    private final WidgetListSchematicVerificationResults listWidget;
    private final SchematicVerifier verifier;
    private final GuiSchematicVerifier.BlockMismatchEntry mismatchEntry;
    @Nullable
    private final String header1;
    @Nullable
    private final String header2;
    @Nullable
    private final String header3;
    @Nullable
    private final BlockMismatchInfo mismatchInfo;
    private final int count;
    private final boolean isOdd;
    @Nullable
    private final ButtonGeneric buttonIgnore;

    public WidgetSchematicVerificationResult(int x, int y, int width, int height, boolean isOdd, WidgetListSchematicVerificationResults listWidget, GuiSchematicVerifier guiSchematicVerifier, GuiSchematicVerifier.BlockMismatchEntry entry, int listIndex) {
        super(x, y, width, height, (Object)entry, listIndex);
        this.columnCount = 3;
        this.blockModelShapes = this.mc.method_1541();
        this.mismatchEntry = entry;
        this.guiSchematicVerifier = guiSchematicVerifier;
        this.listWidget = listWidget;
        this.verifier = guiSchematicVerifier.getPlacement().getSchematicVerifier();
        this.isOdd = isOdd;
        if (entry.header1 != null && entry.header2 != null) {
            this.header1 = entry.header1;
            this.header2 = entry.header2;
            this.header3 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADER_COUNT, (Object[])new Object[0]) + GuiBase.TXT_RST;
            this.mismatchInfo = null;
            this.count = 0;
            this.buttonIgnore = null;
        } else if (entry.header1 != null) {
            this.header1 = entry.header1;
            this.header2 = null;
            this.header3 = null;
            this.mismatchInfo = null;
            this.count = 0;
            this.buttonIgnore = null;
        } else {
            this.header1 = null;
            this.header2 = null;
            this.header3 = null;
            this.mismatchInfo = new BlockMismatchInfo(entry.blockMismatch.stateExpected, entry.blockMismatch.stateFound);
            this.count = entry.blockMismatch.count;
            this.buttonIgnore = entry.mismatchType != SchematicVerifier.MismatchType.CORRECT_STATE ? this.createButton(this.x + this.width, y + 1, ButtonListener.ButtonType.IGNORE_MISMATCH) : null;
        }
    }

    public static void setMaxNameLengths(List<SchematicVerifier.BlockMismatch> mismatches) {
        maxNameLengthExpected = StringUtils.getStringWidth((String)(GuiBase.TXT_BOLD + StringUtils.translate((String)HEADER_EXPECTED, (Object[])new Object[0]) + GuiBase.TXT_RST));
        maxNameLengthFound = StringUtils.getStringWidth((String)(GuiBase.TXT_BOLD + StringUtils.translate((String)HEADER_FOUND, (Object[])new Object[0]) + GuiBase.TXT_RST));
        maxCountLength = 7 * StringUtils.getStringWidth((String)"8");
        for (SchematicVerifier.BlockMismatch entry : mismatches) {
            class_1799 stack = ItemUtils.getItemForState(entry.stateExpected);
            String name = BlockMismatchInfo.getDisplayName(entry.stateExpected, stack);
            maxNameLengthExpected = Math.max(maxNameLengthExpected, StringUtils.getStringWidth((String)name));
            stack = ItemUtils.getItemForState(entry.stateFound);
            name = BlockMismatchInfo.getDisplayName(entry.stateFound, stack);
            maxNameLengthFound = Math.max(maxNameLengthFound, StringUtils.getStringWidth((String)name));
        }
        maxCountLength = Math.max(maxCountLength, StringUtils.getStringWidth((String)(GuiBase.TXT_BOLD + StringUtils.translate((String)HEADER_COUNT, (Object[])new Object[0]) + GuiBase.TXT_RST)));
    }

    private ButtonGeneric createButton(int x, int y, ButtonListener.ButtonType type) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, true, type.getDisplayName(), new Object[0]);
        return (ButtonGeneric)this.addButton((ButtonBase)button, new ButtonListener(type, this.mismatchEntry, this.guiSchematicVerifier));
    }

    protected int getCurrentSortColumn() {
        return this.verifier.getSortCriteria().ordinal();
    }

    protected boolean getSortInReverse() {
        return this.verifier.getSortInReverse();
    }

    protected int getColumnPosX(int column) {
        int x1 = this.x + 4;
        int x2 = x1 + maxNameLengthExpected + 40;
        int x3 = x2 + maxNameLengthFound + 40;
        switch (column) {
            case 0: {
                return x1;
            }
            case 1: {
                return x2;
            }
            case 2: {
                return x3;
            }
            case 3: {
                return x3 + maxCountLength + 20;
            }
        }
        return x1;
    }

    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (super.onMouseClickedImpl(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (this.mismatchEntry.type != GuiSchematicVerifier.BlockMismatchEntry.Type.HEADER) {
            return false;
        }
        int column = this.getMouseOverColumn(mouseX, mouseY);
        switch (column) {
            case 0: {
                this.verifier.setSortCriteria(SchematicVerifier.SortCriteria.NAME_EXPECTED);
                break;
            }
            case 1: {
                this.verifier.setSortCriteria(SchematicVerifier.SortCriteria.NAME_FOUND);
                break;
            }
            case 2: {
                this.verifier.setSortCriteria(SchematicVerifier.SortCriteria.COUNT);
                break;
            }
            default: {
                return false;
            }
        }
        this.listWidget.refreshEntries();
        return true;
    }

    public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
        return (this.buttonIgnore == null || mouseX < this.buttonIgnore.getX()) && super.canSelectAt(mouseX, mouseY, mouseButton);
    }

    protected boolean shouldRenderAsSelected() {
        if (this.mismatchEntry.type == GuiSchematicVerifier.BlockMismatchEntry.Type.CATEGORY_TITLE) {
            return this.verifier.isMismatchCategorySelected(this.mismatchEntry.mismatchType);
        }
        if (this.mismatchEntry.type == GuiSchematicVerifier.BlockMismatchEntry.Type.DATA) {
            return this.verifier.isMismatchEntrySelected(this.mismatchEntry.blockMismatch);
        }
        return false;
    }

    public void render(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        selected = this.shouldRenderAsSelected();
        int color = -1607454672;
        if (selected) {
            color = -1603243920;
        } else if (this.isMouseOver(mouseX, mouseY)) {
            color = -1605349296;
        } else if (this.isOdd) {
            color = -1609560048;
        }
        fi.dy.masa.malilib.render.RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)color);
        if (selected) {
            fi.dy.masa.malilib.render.RenderUtils.drawOutline((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-2039584);
        }
        int x1 = this.getColumnPosX(0);
        int x2 = this.getColumnPosX(1);
        int x3 = this.getColumnPosX(2);
        int y = this.y + 7;
        color = -1;
        if (this.header1 != null && this.header2 != null) {
            this.drawString(x1, y, color, this.header1, drawContext);
            this.drawString(x2, y, color, this.header2, drawContext);
            this.drawString(x3, y, color, this.header3, drawContext);
            this.renderColumnHeader(mouseX, mouseY, Icons.ARROW_DOWN, Icons.ARROW_UP, drawContext);
        } else if (this.header1 != null) {
            this.drawString(this.x + 4, this.y + 7, color, this.header1, drawContext);
        } else if (!(this.mismatchInfo == null || this.mismatchEntry.mismatchType == SchematicVerifier.MismatchType.CORRECT_STATE && this.mismatchEntry.blockMismatch.stateExpected.method_26215())) {
            class_1087 model;
            boolean useBlockModelFound;
            this.drawString(x1 + 20, y, color, this.mismatchInfo.nameExpected, drawContext);
            if (this.mismatchEntry.mismatchType != SchematicVerifier.MismatchType.CORRECT_STATE) {
                this.drawString(x2 + 20, y, color, this.mismatchInfo.nameFound, drawContext);
            }
            this.drawString(x3, y, color, String.valueOf(this.count), drawContext);
            y = this.y + 3;
            fi.dy.masa.malilib.render.RenderUtils.drawRect((int)x1, (int)y, (int)16, (int)16, (int)0x20FFFFFF);
            boolean useBlockModelConfig = Configs.Visuals.SCHEMATIC_VERIFIER_BLOCK_MODELS.getBooleanValue();
            boolean hasModelExpected = this.mismatchInfo.stateExpected.method_26217() == class_2464.field_11458;
            boolean hasModelFound = this.mismatchInfo.stateFound.method_26217() == class_2464.field_11458;
            boolean isAirItemExpected = this.mismatchInfo.stackExpected.method_7960();
            boolean isAirItemFound = this.mismatchInfo.stackFound.method_7960();
            boolean useBlockModelExpected = hasModelExpected && (isAirItemExpected || useBlockModelConfig || this.mismatchInfo.stateExpected.method_26204() == class_2246.field_10495);
            boolean bl = useBlockModelFound = hasModelFound && (isAirItemFound || useBlockModelConfig || this.mismatchInfo.stateFound.method_26204() == class_2246.field_10495);
            if (useBlockModelExpected) {
                model = this.blockModelShapes.method_3349(this.mismatchInfo.stateExpected);
                WidgetSchematicVerificationResult.renderModelInGui(x1, y, 1.0f, model, this.mismatchInfo.stateExpected, drawContext, this.mc);
            } else {
                drawContext.method_51427(this.mismatchInfo.stackExpected, x1, y);
                drawContext.method_51431(this.textRenderer, this.mismatchInfo.stackExpected, x1, y);
            }
            if (this.mismatchEntry.mismatchType != SchematicVerifier.MismatchType.CORRECT_STATE) {
                fi.dy.masa.malilib.render.RenderUtils.drawRect((int)x2, (int)y, (int)16, (int)16, (int)0x20FFFFFF);
                if (useBlockModelFound) {
                    model = this.blockModelShapes.method_3349(this.mismatchInfo.stateFound);
                    WidgetSchematicVerificationResult.renderModelInGui(x2, y, 1.0f, model, this.mismatchInfo.stateFound, drawContext, this.mc);
                } else {
                    drawContext.method_51427(this.mismatchInfo.stackFound, x2, y);
                    drawContext.method_51431(this.textRenderer, this.mismatchInfo.stackFound, x2, y);
                }
            }
            RenderSystem.disableBlend();
        }
        super.render(mouseX, mouseY, selected, drawContext);
    }

    public void postRenderHovered(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        if (this.mismatchInfo != null && this.buttonIgnore != null && mouseX < this.buttonIgnore.getX()) {
            class_4587 matrixStack = drawContext.method_51448();
            matrixStack.method_22903();
            matrixStack.method_46416(0.0f, 0.0f, 200.0f);
            int x = mouseX + 10;
            int y = mouseY;
            int width = this.mismatchInfo.getTotalWidth();
            int height = this.mismatchInfo.getTotalHeight();
            if (x + width > GuiUtils.getCurrentScreenWidth()) {
                x = mouseX - width - 10;
            }
            if (y + height > GuiUtils.getCurrentScreenHeight()) {
                y = mouseY - height - 2;
            }
            this.mismatchInfo.toggleUseBackgroundMask(true);
            this.mismatchInfo.render(x, y, this.mc, drawContext);
            matrixStack.method_22909();
        }
    }

    public static void renderModelInGui(int x, int y, float z, class_1087 model, class_2680 state, class_332 drawContext, class_310 mc) {
        class_4587 matrixStack = drawContext.method_51448();
        if (state.method_26204() == class_2246.field_10124) {
            return;
        }
        fi.dy.masa.malilib.render.RenderUtils.bindTexture((class_2960)class_1059.field_5275);
        mc.method_1531().method_4619(class_1059.field_5275).method_4527(false, false);
        fi.dy.masa.malilib.render.RenderUtils.setupBlend();
        fi.dy.masa.malilib.render.RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        matrixStack.method_22903();
        matrixStack.method_22904((double)x + 8.0, (double)y + 8.0, (double)z + 100.0);
        matrixStack.method_22905(16.0f, -16.0f, 16.0f);
        Quaternionf rot = new Quaternionf().rotationXYZ(0.5235988f, 3.9269907f, 0.0f);
        matrixStack.method_22907(rot);
        matrixStack.method_22905(0.625f, 0.625f, 0.625f);
        matrixStack.method_22904(-0.5, -0.5, -0.5);
        WidgetSchematicVerificationResult.renderModel(model, state, matrixStack);
        matrixStack.method_22909();
    }

    private static void renderModel(class_1087 model, class_2680 state, class_4587 matrixStack) {
        class_4597.class_4598 immediate = class_4597.method_22991((class_9799)new class_9799(1536));
        class_4588 vertexConsumer = immediate.getBuffer(class_1921.method_23583());
        class_4587.class_4665 matrixEntry = matrixStack.method_23760();
        int l = class_765.method_23687((int)15, (int)15);
        int[] light = new int[]{l, l, l, l};
        float[] brightness = new float[]{0.75f, 0.75f, 0.75f, 1.0f};
        RenderSystem.setShader((class_10156)class_10142.field_53884);
        class_308.method_24211();
        for (class_2350 face : PositionUtils.ALL_DIRECTIONS) {
            RAND.method_43052(0L);
            WidgetSchematicVerificationResult.renderQuads(model.method_4707(state, face, (class_5819)RAND), brightness, light, matrixEntry, vertexConsumer);
        }
        RAND.method_43052(0L);
        WidgetSchematicVerificationResult.renderQuads(model.method_4707(state, null, (class_5819)RAND), brightness, light, matrixEntry, vertexConsumer);
        immediate.method_22993();
    }

    private static void renderQuads(List<class_777> quads, float[] brightness, int[] light, class_4587.class_4665 matrixEntry, class_4588 vertexConsumer) {
        for (class_777 quad : quads) {
            WidgetSchematicVerificationResult.renderQuad(quad, brightness, light, matrixEntry, vertexConsumer);
        }
    }

    private static void renderQuad(class_777 quad, float[] brightness, int[] light, class_4587.class_4665 matrixEntry, class_4588 vertexConsumer) {
        vertexConsumer.method_22920(matrixEntry, quad, brightness, 1.0f, 1.0f, 1.0f, 1.0f, light, class_4608.field_21444, true);
    }

    public static class BlockMismatchInfo {
        private final class_2680 stateExpected;
        private final class_2680 stateFound;
        private final class_1799 stackExpected;
        private final class_1799 stackFound;
        private final String blockRegistrynameExpected;
        private final String blockRegistrynameFound;
        private final String nameExpected;
        private final String nameFound;
        private final int totalWidth;
        private final int totalHeight;
        private final int columnWidthExpected;
        private boolean useBackgroundMask = false;

        public BlockMismatchInfo(class_2680 stateExpected, class_2680 stateFound) {
            this.stateExpected = stateExpected;
            this.stateFound = stateFound;
            this.stackExpected = ItemUtils.getItemForState(this.stateExpected);
            this.stackFound = ItemUtils.getItemForState(this.stateFound);
            class_2248 blockExpected = this.stateExpected.method_26204();
            class_2248 blockFound = this.stateFound.method_26204();
            class_2960 rl1 = class_7923.field_41175.method_10221((Object)blockExpected);
            class_2960 rl2 = class_7923.field_41175.method_10221((Object)blockFound);
            this.blockRegistrynameExpected = rl1 != null ? rl1.toString() : "<null>";
            this.blockRegistrynameFound = rl2 != null ? rl2.toString() : "<null>";
            this.nameExpected = BlockMismatchInfo.getDisplayName(stateExpected, this.stackExpected);
            this.nameFound = BlockMismatchInfo.getDisplayName(stateFound, this.stackFound);
            List propsExpected = BlockUtils.getFormattedBlockStateProperties((class_2680)this.stateExpected, (String)" = ");
            List propsFound = BlockUtils.getFormattedBlockStateProperties((class_2680)this.stateFound, (String)" = ");
            int w1 = Math.max(StringUtils.getStringWidth((String)this.nameExpected) + 20, StringUtils.getStringWidth((String)this.blockRegistrynameExpected));
            int w2 = Math.max(StringUtils.getStringWidth((String)this.nameFound) + 20, StringUtils.getStringWidth((String)this.blockRegistrynameFound));
            w1 = Math.max(w1, RenderUtils.getMaxStringRenderLength(propsExpected));
            w2 = Math.max(w2, RenderUtils.getMaxStringRenderLength(propsFound));
            this.columnWidthExpected = w1;
            this.totalWidth = this.columnWidthExpected + w2 + 40;
            this.totalHeight = Math.max(propsExpected.size(), propsFound.size()) * (StringUtils.getFontHeight() + 2) + 60;
        }

        public static String getDisplayName(class_2680 state, class_1799 stack) {
            String name;
            class_2248 block = state.method_26204();
            String key = block.method_63499() + ".name";
            name = !key.equals(name = StringUtils.translate((String)key, (Object[])new Object[0])) ? name : stack.method_7964().getString();
            return name;
        }

        public int getTotalWidth() {
            return this.totalWidth;
        }

        public int getTotalHeight() {
            return this.totalHeight;
        }

        public void toggleUseBackgroundMask(boolean toggle) {
            this.useBackgroundMask = toggle;
        }

        public void render(int x, int y, class_310 mc, class_332 drawContext) {
            if (this.stateExpected != null && this.stateFound != null) {
                class_1087 model;
                if (this.useBackgroundMask) {
                    RenderUtils.renderBackgroundMask(x + 1, y + 1, this.totalWidth - 1, this.totalHeight - 1, drawContext);
                }
                class_4587 matrixStack = drawContext.method_51448();
                matrixStack.method_22903();
                fi.dy.masa.malilib.render.RenderUtils.drawOutlinedBox((int)x, (int)y, (int)this.totalWidth, (int)this.totalHeight, (int)-16777216, (int)-6710887);
                int x1 = x + 10;
                int x2 = x + this.columnWidthExpected + 30;
                class_327 textRenderer = mc.field_1772;
                String pre = GuiBase.TXT_WHITE + GuiBase.TXT_BOLD;
                String strExpected = pre + StringUtils.translate((String)WidgetSchematicVerificationResult.HEADER_EXPECTED, (Object[])new Object[0]) + GuiBase.TXT_RST;
                String strFound = pre + StringUtils.translate((String)WidgetSchematicVerificationResult.HEADER_FOUND, (Object[])new Object[0]) + GuiBase.TXT_RST;
                drawContext.method_51433(textRenderer, strExpected, x1, y += 4, -1, false);
                drawContext.method_51433(textRenderer, strFound, x2, y, -1, false);
                y += 12;
                boolean useBlockModelConfig = Configs.Visuals.SCHEMATIC_VERIFIER_BLOCK_MODELS.getBooleanValue();
                boolean hasModelExpected = this.stateExpected.method_26217() == class_2464.field_11458;
                boolean hasModelFound = this.stateFound.method_26217() == class_2464.field_11458;
                boolean isAirItemExpected = this.stackExpected.method_7960();
                boolean isAirItemFound = this.stackFound.method_7960();
                boolean useBlockModelExpected = hasModelExpected && (isAirItemExpected || useBlockModelConfig || this.stateExpected.method_26204() == class_2246.field_10495);
                boolean useBlockModelFound = hasModelFound && (isAirItemFound || useBlockModelConfig || this.stateFound.method_26204() == class_2246.field_10495);
                class_776 blockModelShapes = mc.method_1541();
                fi.dy.masa.malilib.render.RenderUtils.drawRect((int)x1, (int)y, (int)16, (int)16, (int)0x50C0C0C0);
                fi.dy.masa.malilib.render.RenderUtils.drawRect((int)x2, (int)y, (int)16, (int)16, (int)0x50C0C0C0);
                int iconY = y;
                drawContext.method_51433(textRenderer, this.nameExpected, x1 + 20, y + 4, -1, false);
                drawContext.method_51433(textRenderer, this.nameFound, x2 + 20, y + 4, -1, false);
                drawContext.method_51433(textRenderer, this.blockRegistrynameExpected, x1, y += 20, -12558081, false);
                drawContext.method_51433(textRenderer, this.blockRegistrynameFound, x2, y, -12558081, false);
                List propsExpected = BlockUtils.getFormattedBlockStateProperties((class_2680)this.stateExpected, (String)" = ");
                List propsFound = BlockUtils.getFormattedBlockStateProperties((class_2680)this.stateFound, (String)" = ");
                fi.dy.masa.malilib.render.RenderUtils.renderText((int)x1, (int)(y += StringUtils.getFontHeight() + 4), (int)-5197648, (List)propsExpected, (class_332)drawContext);
                fi.dy.masa.malilib.render.RenderUtils.renderText((int)x2, (int)y, (int)-5197648, (List)propsFound, (class_332)drawContext);
                if (useBlockModelExpected) {
                    model = blockModelShapes.method_3349(this.stateExpected);
                    WidgetSchematicVerificationResult.renderModelInGui(x1, iconY, 1.0f, model, this.stateExpected, drawContext, mc);
                } else {
                    drawContext.method_51427(this.stackExpected, x1, iconY);
                    drawContext.method_51431(textRenderer, this.stackExpected, x1, iconY);
                }
                if (useBlockModelFound) {
                    model = blockModelShapes.method_3349(this.stateFound);
                    WidgetSchematicVerificationResult.renderModelInGui(x2, iconY, 1.0f, model, this.stateFound, drawContext, mc);
                } else {
                    drawContext.method_51427(this.stackFound, x2, iconY);
                    drawContext.method_51431(textRenderer, this.stackFound, x2, iconY);
                }
                matrixStack.method_22909();
            }
        }
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final ButtonType type;
        private final GuiSchematicVerifier guiSchematicVerifier;
        private final GuiSchematicVerifier.BlockMismatchEntry mismatchEntry;

        public ButtonListener(ButtonType type, GuiSchematicVerifier.BlockMismatchEntry mismatchEntry, GuiSchematicVerifier guiSchematicVerifier) {
            this.type = type;
            this.mismatchEntry = mismatchEntry;
            this.guiSchematicVerifier = guiSchematicVerifier;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == ButtonType.IGNORE_MISMATCH) {
                this.guiSchematicVerifier.getPlacement().getSchematicVerifier().ignoreStateMismatch(this.mismatchEntry.blockMismatch);
                this.guiSchematicVerifier.initGui();
            }
        }

        public static enum ButtonType {
            IGNORE_MISMATCH("litematica.gui.button.schematic_verifier.ignore");

            private final String translationKey;

            private ButtonType(String translationKey) {
                this.translationKey = translationKey;
            }

            public String getDisplayName() {
                return StringUtils.translate((String)this.translationKey, (Object[])new Object[0]);
            }
        }
    }
}

