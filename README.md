# Litematica (bản fork tuỳ biến) — project Gradle để build lại

Project này được dựng từ:
- Source `.java` decompile bằng CFR từ `mod-fabric-1.21.4.jar` (274 file, gồm
  toàn bộ Litematica đã chỉnh sửa + các module macro riêng: `com.pb`,
  `com.autoclick`, `com.cauca`, `com.atj`, `com.ghepdo`, `com.luyendan`,
  `com.mayquetlinhthao`, `com.lvt.guard`).
- Resource gốc lấy trực tiếp từ jar (`fabric.mod.json`, `mixins.litematica.json`,
  `litematica.accesswidener`, `assets/`).
- `com/pb/AutoPhoban.java` **đã được sửa** lỗi `VerifyError: Inconsistent
  stackmap frames` (nguyên nhân: pattern "gán trong điều kiện + cast tay"
  do Recaf sinh ra khi bạn sửa file trước đó). Đã thay bằng pattern matching
  `instanceof` chuẩn của Java 21.

## ⚠️ Vì sao mình (Claude) không tự build/test được sẵn

Sandbox chạy Claude bị chặn mạng, không truy cập được
`maven.fabricmc.net`, kho Mojang, hay `masa.dy.fi` — tức là **không tải được
Minecraft, Fabric Loom, hay malilib** để thử compile. Project này được
chuẩn bị đầy đủ nhưng **chưa được build-test thực tế**. Việc build thật sự
sẽ diễn ra trên máy bạn hoặc trên GitHub Actions (cả hai đều có mạng đầy đủ).
Nhiều khả năng sẽ cần sửa vài chỗ nhỏ khi gặp lỗi build lần đầu — xem mục
"Các điểm cần kiểm tra lại" bên dưới.

## Việc cần làm trước khi build

### 1. Thêm file malilib
Xem hướng dẫn trong `libs/PLACE_MALILIB_JAR_HERE.txt`. Bắt buộc phải có vì
Litematica gọi rất nhiều class của malilib.

### 2. Build
```bash
./gradlew build
```
(Windows: `gradlew.bat build`)

Jar kết quả nằm ở `build/libs/litematica-0.21.6.jar`.

### 3. Hoặc để GitHub Actions build giúp
Repo đã có sẵn `.github/workflows/build.yml`. Chỉ cần push code lên GitHub,
vào tab **Actions** để theo dõi, tải file jar ở mục **Artifacts** khi build
xong.

```bash
git init
git add .
git commit -m "Initial import + fix AutoPhoban VerifyError"
git branch -M main
git remote add origin https://github.com/<ten-ban>/<ten-repo>.git
git push -u origin main
```

## Các điểm cần kiểm tra lại (khả năng cao sẽ gặp khi build lần đầu)

1. **Mapping intermediary**: `build.gradle` dùng
   `mappings "net.fabricmc:intermediary:${minecraft_version}:v2"` để source
   dạng `class_310`, `method_7353`... biên dịch thẳng không cần đổi tên.
   Đây là kỹ thuật ít phổ biến hơn Yarn thông thường; nếu Loom báo lỗi vì
   thiếu namespace "named", cách khắc phục: dùng Yarn mappings bình thường
   (`mappings loom.officialMojangMappings()` hoặc bản Yarn ứng với
   1.21.4) rồi chạy `./gradlew genSources`, nhưng khi đó sẽ cần **đổi lại
   toàn bộ tên class_XXX/method_XXX/field_XXX trong 274 file source** sang
   tên Yarn tương ứng — việc này không thể làm thủ công, cần một script ánh
   xạ mapping (mình có thể giúp viết nếu bạn cần đến bước này).

2. **Tên artifact malilib** trong `build.gradle`
   (`malilib-fabric-1.21.4-0.23.5`) là suy đoán hợp lý dựa trên quy ước đặt
   tên thường thấy — sửa lại đúng tên file bạn đặt trong `libs/` nếu khác.

3. **Access widener / mixin refmap**: đã copy nguyên bản
   `litematica.accesswidener` và `mixins.litematica.json` từ jar gốc, và
   `build.gradle` đã trỏ `loom.accessWidenerPath` đúng chỗ — phần này nhìn
   chung không cần chỉnh.

4. Nếu compile báo lỗi ở file khác ngoài `AutoPhoban.java` (274 file được
   CFR decompile tự động, phần lớn chưa qua kiểm tra thủ công), gửi lại
   thông báo lỗi, mình sẽ sửa tiếp từng file như đã làm với `AutoPhoban.java`.

## Giấy phép
Litematica gốc thuộc bản quyền tác giả `maruohon`, phát hành theo giấy phép
LGPLv3 (mã nguồn: https://github.com/maruohon/litematica). Bản fork này giữ
nguyên các phần Litematica gốc và thêm các module riêng nêu trên.
