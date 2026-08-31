/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.data.DataDump
 *  fi.dy.masa.malilib.data.DataDump$Alignment
 *  fi.dy.masa.malilib.data.DataDump$Format
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.GuiListBase
 *  fi.dy.masa.malilib.gui.GuiTextFieldGeneric
 *  fi.dy.masa.malilib.gui.GuiTextFieldInteger
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.ButtonOnOff
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.interfaces.IGuiIcon
 *  fi.dy.masa.malilib.gui.interfaces.ITextFieldListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetBase
 *  fi.dy.masa.malilib.gui.widgets.WidgetInfoIcon
 *  fi.dy.masa.malilib.interfaces.ICompletionListener
 *  fi.dy.masa.malilib.util.FileUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  net.minecraft.class_1657
 */
package fi.dy.masa.litematica.gui;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiMainMenu;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.gui.widgets.WidgetListMaterialList;
import fi.dy.masa.litematica.gui.widgets.WidgetMaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.materials.MaterialListAreaAnalyzer;
import fi.dy.masa.litematica.materials.MaterialListBase;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import fi.dy.masa.litematica.materials.MaterialListSorter;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.util.BlockInfoListType;
import fi.dy.masa.malilib.data.DataDump;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ButtonOnOff;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetInfoIcon;
import fi.dy.masa.malilib.interfaces.ICompletionListener;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_1657;

public class GuiMaterialList
extends GuiListBase<MaterialListEntry, WidgetMaterialListEntry, WidgetListMaterialList>
implements ICompletionListener {
    private final MaterialListBase materialList;

    public GuiMaterialList(MaterialListBase materialList) {
        super(10, 44);
        this.materialList = materialList;
        this.materialList.setCompletionListener(this);
        this.title = this.materialList.getTitle();
        this.useTitleHierarchy = false;
        MaterialListUtils.updateAvailableCounts(this.materialList.getMaterialsAll(), (class_1657)this.mc.field_1724);
        WidgetMaterialListEntry.setMaxNameLength(materialList.getMaterialsAll(), materialList.getMultiplier());
        if (DataManager.getMaterialList() == null) {
            DataManager.setMaterialList(materialList);
        }
    }

    protected int getBrowserWidth() {
        return this.getScreenWidth() - 20;
    }

    protected int getBrowserHeight() {
        return this.getScreenHeight() - 80;
    }

    public void initGui() {
        super.initGui();
        boolean isNarrow = this.getScreenWidth() < this.getElementTotalWidth();
        int x = 12;
        int y = 24;
        Object str = StringUtils.translate((String)"litematica.gui.label.material_list.multiplier", (Object[])new Object[0]);
        int w = this.getStringWidth((String)str);
        this.addLabel(this.getScreenWidth() - w - 56, y + 5, w, 12, -1, new String[]{str});
        GuiTextFieldInteger tf = new GuiTextFieldInteger(this.getScreenWidth() - 52, y + 2, 40, 16, this.textRenderer);
        tf.setTextWrapper(String.valueOf(this.materialList.getMultiplier()));
        MultiplierListener listener = new MultiplierListener(this.materialList, this);
        this.addTextField((GuiTextFieldGeneric)tf, listener);
        this.addWidget((WidgetBase)new WidgetInfoIcon(this.getScreenWidth() - 23, 10, (IGuiIcon)Icons.INFO_11, "litematica.info.material_list", new Object[0]));
        int gap = 1;
        x += this.createButton(x, y, -1, ButtonListener.Type.REFRESH_LIST) + gap;
        if (this.materialList.supportsRenderLayers()) {
            x += this.createButton(x, y, -1, ButtonListener.Type.LIST_TYPE) + gap;
        }
        x += this.createButtonOnOff(x, y, -1, this.materialList.getHideAvailable(), ButtonListener.Type.HIDE_AVAILABLE) + gap;
        x += this.createButtonOnOff(x, y, -1, this.materialList.getHudRenderer().getShouldRenderCustom(), ButtonListener.Type.TOGGLE_INFO_HUD) + gap;
        if (isNarrow) {
            x = 12;
            y = this.getScreenHeight() - 22;
        }
        x += this.createButton(x, y, -1, ButtonListener.Type.CLEAR_IGNORED) + gap;
        x += this.createButton(x, y, -1, ButtonListener.Type.CLEAR_CACHE) + gap;
        x += this.createButton(x, y, -1, ButtonListener.Type.WRITE_TO_FILE) + gap;
        y += 22;
        y = this.getScreenHeight() - 36;
        GuiMainMenu.ButtonListenerChangeMenu.ButtonType type = GuiMainMenu.ButtonListenerChangeMenu.ButtonType.MAIN_MENU;
        String label = StringUtils.translate((String)type.getLabelKey(), (Object[])new Object[0]);
        int buttonWidth = this.getStringWidth(label) + 20;
        x = this.getScreenWidth() - buttonWidth - 10;
        ButtonGeneric button = new ButtonGeneric(x, y, buttonWidth, 20, label, new String[0]);
        this.addButton((ButtonBase)button, new GuiMainMenu.ButtonListenerChangeMenu(type, this.getParent()));
        long total = this.materialList.getCountTotal();
        long missing = this.materialList.getCountMissing() - this.materialList.getCountMismatched();
        long mismatch = this.materialList.getCountMismatched();
        if (total != 0L && !(this.materialList instanceof MaterialListAreaAnalyzer)) {
            String strp;
            double pctDone = (double)(total - (missing + mismatch)) / (double)total * 100.0;
            double pctMissing = (double)missing / (double)total * 100.0;
            double pctMismatch = (double)mismatch / (double)total * 100.0;
            String strt = StringUtils.translate((String)"litematica.gui.label.material_list.total", (Object[])new Object[]{total});
            if (missing == 0L && mismatch == 0L) {
                strp = StringUtils.translate((String)"litematica.gui.label.material_list.progress.done", (Object[])new Object[]{String.format("%.0f %%%%", pctDone)});
            } else {
                String str1 = StringUtils.translate((String)"litematica.gui.label.material_list.progress.done", (Object[])new Object[]{String.format("%.1f %%%%", pctDone)});
                String str2 = StringUtils.translate((String)"litematica.gui.label.material_list.progress.missing", (Object[])new Object[]{String.format("%.1f %%%%", pctMissing)});
                String str3 = StringUtils.translate((String)"litematica.gui.label.material_list.progress.mismatch", (Object[])new Object[]{String.format("%.1f %%%%", pctMismatch)});
                strp = String.format("%s / %s / %s", str1, str2, str3);
            }
            str = strt + " / " + StringUtils.translate((String)"litematica.gui.label.material_list.progress", (Object[])new Object[]{strp});
            w = this.getStringWidth((String)str);
            this.addLabel(12, this.getScreenHeight() - 36, w, 12, -1, new String[]{str});
        }
    }

    private int createButton(int x, int y, int width, ButtonListener.Type type) {
        ButtonListener listener = new ButtonListener(type, this);
        String label = "";
        label = type == ButtonListener.Type.LIST_TYPE ? type.getDisplayName(this.materialList.getMaterialListType().getDisplayName()) : type.getDisplayName(new Object[0]);
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, label, new String[0]);
        if (type == ButtonListener.Type.CLEAR_CACHE) {
            button.setHoverStrings(new String[]{"litematica.gui.button.hover.material_list.clear_cache"});
        } else if (type == ButtonListener.Type.WRITE_TO_FILE) {
            button.setHoverStrings(new String[]{"litematica.gui.button.hover.material_list.write_hold_shift_for_csv"});
        }
        this.addButton((ButtonBase)button, listener);
        return button.getWidth();
    }

    private int getElementTotalWidth() {
        int width = 0;
        width += this.getStringWidth(ButtonListener.Type.REFRESH_LIST.getDisplayName(new Object[0]));
        width += this.getStringWidth(ButtonListener.Type.LIST_TYPE.getDisplayName(this.materialList.getMaterialListType().getDisplayName()));
        width += this.getStringWidth(ButtonListener.Type.CLEAR_IGNORED.getDisplayName(new Object[0]));
        width += this.getStringWidth(ButtonListener.Type.CLEAR_CACHE.getDisplayName(new Object[0]));
        width += this.getStringWidth(ButtonListener.Type.WRITE_TO_FILE.getDisplayName(new Object[0]));
        width += new ButtonOnOff(0, 0, -1, false, ButtonListener.Type.HIDE_AVAILABLE.getTranslationKey(), false, new String[0]).getWidth();
        width += new ButtonOnOff(0, 0, -1, false, ButtonListener.Type.TOGGLE_INFO_HUD.getTranslationKey(), false, new String[0]).getWidth();
        width += this.getStringWidth(StringUtils.translate((String)"litematica.gui.label.material_list.multiplier", (Object[])new Object[0]));
        return width += 130;
    }

    private int createButtonOnOff(int x, int y, int width, boolean isCurrentlyOn, ButtonListener.Type type) {
        ButtonOnOff button = new ButtonOnOff(x, y, width, false, type.getTranslationKey(), isCurrentlyOn, new String[0]);
        this.addButton((ButtonBase)button, new ButtonListener(type, this));
        return button.getWidth();
    }

    public MaterialListBase getMaterialList() {
        return this.materialList;
    }

    public void onTaskCompleted() {
        if (GuiUtils.getCurrentScreen() == this) {
            WidgetMaterialListEntry.setMaxNameLength(this.materialList.getMaterialsAll(), this.materialList.getMultiplier());
            this.initGui();
        }
    }

    protected WidgetListMaterialList createListWidget(int listX, int listY) {
        return new WidgetListMaterialList(listX, listY, this.getBrowserWidth(), this.getBrowserHeight(), this);
    }

    private static class MultiplierListener
    implements ITextFieldListener<GuiTextFieldInteger> {
        private final MaterialListBase materialList;
        private final GuiMaterialList gui;

        private MultiplierListener(MaterialListBase materialList, GuiMaterialList gui) {
            this.materialList = materialList;
            this.gui = gui;
        }

        public boolean onTextChange(GuiTextFieldInteger textField) {
            try {
                int multiplier = Integer.parseInt(textField.getTextWrapper());
                if (multiplier != this.materialList.getMultiplier()) {
                    this.materialList.setMultiplier(multiplier);
                    ((WidgetListMaterialList)this.gui.getListWidget()).refreshEntries();
                    return true;
                }
            }
            catch (Exception e) {
                this.materialList.setMultiplier(1);
                ((WidgetListMaterialList)this.gui.getListWidget()).refreshEntries();
            }
            return false;
        }
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final GuiMaterialList parent;
        private final Type type;

        public ButtonListener(Type type, GuiMaterialList parent) {
            this.parent = parent;
            this.type = type;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            MaterialListBase materialList = this.parent.materialList;
            switch (this.type.ordinal()) {
                case 0: {
                    materialList.reCreateMaterialList();
                    break;
                }
                case 1: {
                    BlockInfoListType type = materialList.getMaterialListType();
                    materialList.setMaterialListType((BlockInfoListType)type.cycle(mouseButton == 0));
                    materialList.reCreateMaterialList();
                    break;
                }
                case 2: {
                    materialList.setHideAvailable(!materialList.getHideAvailable());
                    materialList.refreshPreFilteredList();
                    materialList.recreateFilteredList();
                    break;
                }
                case 3: {
                    MaterialListHudRenderer renderer = materialList.getHudRenderer();
                    renderer.toggleShouldRender();
                    if (materialList.getHudRenderer().getShouldRenderCustom()) {
                        InfoHud.getInstance().addInfoHudRenderer(renderer, true);
                        break;
                    }
                    InfoHud.getInstance().removeInfoHudRenderersOfType(renderer.getClass(), true);
                    break;
                }
                case 4: {
                    materialList.clearIgnored();
                    break;
                }
                case 5: {
                    MaterialCache.getInstance().clearCache();
                    this.parent.addMessage(Message.MessageType.SUCCESS, 3000, "litematica.message.material_list.material_cache_cleared", new Object[0]);
                    break;
                }
                case 6: {
                    File dir = new File(FileUtils.getConfigDirectoryAsPath().toFile(), "litematica");
                    boolean csv = GuiBase.isShiftDown();
                    String ext = csv ? ".csv" : ".txt";
                    File file = DataDump.dumpDataToFile((File)dir, (String)"material_list", (String)ext, (List)this.getMaterialListDump(materialList, csv).getLines());
                    if (file == null) break;
                    String key = "litematica.message.material_list_written_to_file";
                    this.parent.addMessage(Message.MessageType.SUCCESS, key, new Object[]{file.getName()});
                    StringUtils.sendOpenFileChatMessage((class_1657)this.parent.mc.field_1724, (String)key, (File)file);
                }
            }
            this.parent.initGui();
        }

        private DataDump getMaterialListDump(MaterialListBase materialList, boolean csv) {
            DataDump dump = new DataDump(4, csv ? DataDump.Format.CSV : DataDump.Format.ASCII);
            int multiplier = materialList.getMultiplier();
            ArrayList<MaterialListEntry> list = new ArrayList<MaterialListEntry>();
            list.addAll(materialList.getMaterialsFiltered(false));
            Collections.sort(list, new MaterialListSorter(materialList));
            for (MaterialListEntry entry : list) {
                int total = entry.getCountTotal() * multiplier;
                int missing = multiplier > 1 ? total : entry.getCountMissing();
                int available = entry.getCountAvailable();
                dump.addData(new String[]{entry.getStack().method_7964().getString(), String.valueOf(total), String.valueOf(missing), String.valueOf(available)});
            }
            String titleTotal = multiplier > 1 ? String.format("Total (x%d)", multiplier) : "Total";
            dump.addTitle(new String[]{"Item", titleTotal, "Missing", "Available"});
            dump.addHeader(new String[]{materialList.getTitle()});
            dump.setColumnProperties(1, DataDump.Alignment.RIGHT, true);
            dump.setColumnProperties(2, DataDump.Alignment.RIGHT, true);
            dump.setColumnProperties(3, DataDump.Alignment.RIGHT, true);
            dump.setSort(false);
            dump.setUseColumnSeparator(true);
            return dump;
        }

        public static enum Type {
            REFRESH_LIST("litematica.gui.button.material_list.refresh_list"),
            LIST_TYPE("litematica.gui.button.material_list.list_type"),
            HIDE_AVAILABLE("litematica.gui.button.material_list.hide_available"),
            TOGGLE_INFO_HUD("litematica.gui.button.material_list.toggle_info_hud"),
            CLEAR_IGNORED("litematica.gui.button.material_list.clear_ignored"),
            CLEAR_CACHE("litematica.gui.button.material_list.clear_cache"),
            WRITE_TO_FILE("litematica.gui.button.material_list.write_to_file");

            private final String translationKey;

            private Type(String translationKey) {
                this.translationKey = translationKey;
            }

            public String getTranslationKey() {
                return this.translationKey;
            }

            public String getDisplayName(Object ... args) {
                return StringUtils.translate((String)this.translationKey, (Object[])args);
            }
        }
    }
}

