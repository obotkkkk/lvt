/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.data.CachedItemTags
 *  fi.dy.masa.malilib.data.CachedTagKey
 *  net.minecraft.class_1802
 *  net.minecraft.class_7923
 */
package fi.dy.masa.litematica.data;

import fi.dy.masa.malilib.data.CachedItemTags;
import fi.dy.masa.malilib.data.CachedTagKey;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1802;
import net.minecraft.class_7923;

public class CachedTagManager {
    public static final CachedTagKey GLASS_ITEMS_KEY = new CachedTagKey("litematica", "glass_items");
    public static final CachedTagKey GLASS_PANE_ITEMS_KEY = new CachedTagKey("litematica", "glass_pane_items");
    public static final CachedTagKey CONCRETE_POWDER_ITEMS_KEY = new CachedTagKey("litematica", "concrete_powder_items");
    public static final CachedTagKey CONCRETE_ITEMS_KEY = new CachedTagKey("litematica", "concrete_items");
    public static final CachedTagKey GLAZED_TERRACOTTA_ITEMS_KEY = new CachedTagKey("litematica", "glazed_terracotta_items");
    public static final CachedTagKey PACKED_BLOCK_ITEMS_KEY = new CachedTagKey("litematica", "packed_block_items");
    public static final CachedTagKey UNPACKED_BLOCK_ITEMS_KEY = new CachedTagKey("litematica", "unpacked_block_items");

    public List<CachedTagKey> getKeys() {
        ArrayList<CachedTagKey> list = new ArrayList<CachedTagKey>();
        list.add(GLASS_ITEMS_KEY);
        list.add(GLASS_PANE_ITEMS_KEY);
        list.add(CONCRETE_POWDER_ITEMS_KEY);
        list.add(CONCRETE_ITEMS_KEY);
        list.add(GLAZED_TERRACOTTA_ITEMS_KEY);
        list.add(PACKED_BLOCK_ITEMS_KEY);
        list.add(UNPACKED_BLOCK_ITEMS_KEY);
        return list;
    }

    public static void startCache() {
        CachedTagManager.clearCache();
        CachedItemTags.getInstance().build(GLASS_ITEMS_KEY, CachedTagManager.buildGlassItemCache());
        CachedItemTags.getInstance().build(GLASS_PANE_ITEMS_KEY, CachedTagManager.buildGlassPanesItemCache());
        CachedItemTags.getInstance().build(CONCRETE_POWDER_ITEMS_KEY, CachedTagManager.buildConcretePowderItemCache());
        CachedItemTags.getInstance().build(CONCRETE_ITEMS_KEY, CachedTagManager.buildConcreteItemCache());
        CachedItemTags.getInstance().build(GLAZED_TERRACOTTA_ITEMS_KEY, CachedTagManager.buildGlazedTerracottaItemCache());
        CachedItemTags.getInstance().build(PACKED_BLOCK_ITEMS_KEY, CachedTagManager.buildPackedBlockItemCache());
        CachedItemTags.getInstance().build(UNPACKED_BLOCK_ITEMS_KEY, CachedTagManager.buildUnpackedBlockItemCache());
    }

    private static List<String> buildGlassItemCache() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8280).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8410).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8126).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8332).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8685).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8507).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8734).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8869).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8363).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8340).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8243).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8393).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8770).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8838).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8636).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8095).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8483).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_27019).toString());
        return list;
    }

    private static List<String> buildGlassPanesItemCache() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8141).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8157).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8747).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8501).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8085).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8871).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8656).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8196).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8240).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8581).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8119).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8761).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8500).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8739).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8879).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8703).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8736).toString());
        return list;
    }

    private static List<String> buildConcretePowderItemCache() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8516).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8164).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8437).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8593).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8818).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8198).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8764).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8558).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8418).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8336).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8487).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8222).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8690).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8757).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8205).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8582).toString());
        return list;
    }

    private static List<String> buildConcreteItemCache() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8704).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8737).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8762).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8637).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8333).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8120).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8364).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8735).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8839).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8508).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8771).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8127).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8411).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8197).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8686).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8341).toString());
        return list;
    }

    private static List<String> buildGlazedTerracottaItemCache() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8096).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8484).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8394).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8257).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8885).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8244).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8640).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8172).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8649).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8318).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8139).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8277).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8562).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8870).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8889).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8177).toString());
        return list;
    }

    private static List<String> buildPackedBlockItemCache() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8242).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_19060).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8797).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_27071).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8603).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8733).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8494).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_17528).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_21086).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8773).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8055).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_17522).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_22018).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_33506).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_33507).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_33505).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8793).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_55038).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_55039).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8828).toString());
        return list;
    }

    private static List<String> buildUnpackedBlockItemCache() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8606).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8696).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8713).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_27022).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8477).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8687).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8601).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8695).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8397).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_20417).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8426).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8620).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8675).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8759).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8497).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_22020).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8790).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8081).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8725).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_55044).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_55037).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8777).toString());
        list.add(class_7923.field_41178.method_10221((Object)class_1802.field_8861).toString());
        return list;
    }

    private static void clearCache() {
        CachedItemTags.getInstance().clearEntry(GLASS_ITEMS_KEY);
        CachedItemTags.getInstance().clearEntry(GLASS_PANE_ITEMS_KEY);
        CachedItemTags.getInstance().clearEntry(CONCRETE_POWDER_ITEMS_KEY);
        CachedItemTags.getInstance().clearEntry(CONCRETE_ITEMS_KEY);
        CachedItemTags.getInstance().clearEntry(GLAZED_TERRACOTTA_ITEMS_KEY);
        CachedItemTags.getInstance().clearEntry(PACKED_BLOCK_ITEMS_KEY);
        CachedItemTags.getInstance().clearEntry(UNPACKED_BLOCK_ITEMS_KEY);
    }
}

