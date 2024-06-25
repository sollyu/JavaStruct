# JavaStruct

## 说明

JavaStruct是一个用于Java的结构体库，它可以让你像C语言一样使用结构体。

## 使用

最新版本：[![](https://jitpack.io/v/sollyu/JavaStruct.svg)](https://jitpack.io/#sollyu/JavaStruct)

```gradle
dependencies {
    implementation 'com.github.sollyu:JavaStruct:1.1.1'
}
```

### 定义结构体

```java
public class StructArray01Test implements IJavaStruct {
    @JavaStruct.Field(order = 0)
    public byte serverId;

    @JavaStruct.Field(order = 1, sizeof = "data")
    public short length;

    @JavaStruct.Field(order = 2)
    public CommandItem01[] data;
}
```

在想要序列化的类上实现IJavaStruct接口，然后在需要序列化的字段上添加`@JavaStruct.Field`注解。

order表示字段的顺序。

sizeof表示字段的长度，如果是数组，可以使用“数组名”来表示数组的长度。

### 序列化

```java
StructArray01Test input = new StructArray01Test();
input.serverId = 0x01;
CommandItem01 item1 = new CommandItem01((byte) 0x01, (byte) 0x06, (byte) 0x0E);
CommandItem01 item2 = new CommandItem01((byte) 0x00, (byte) 0x06, (byte) 0x0E);
input.data = new CommandItem01[] { item1, item2 };

byte[] output = JavaStruct.pack(input, ByteOrder.LITTLE_ENDIAN);
```
此时output的值为：`01 02 00 01 03 00 01 06 0E 01 03 00 00 06 0E`

这里的`length`字段会自动计算，所以不需要手动赋值。

### 反序列化

```java
String hexString = "01 02 00 01 03 00 01 06 0E 01 03 00 00 06 0E";
byte[] bytes = StringUtils.hexStringToByteArray(hexString);

StructArray01Test output = new StructArray01Test();
JavaStruct.unpack(bytes, output, ByteOrder.LITTLE_ENDIAN);
```

## 支持特性

- [x] 基本类型、数组、结构体、泛型
- [x] 支持大端、小端序
- [x] 支持声明、动态、代码等计算数据长度

## 注意

在使用泛型的时候，需要注意泛型擦除的问题，例如：

```java
public class StructArray01Test<T> implements IJavaStruct {
    @JavaStruct.Field(order = 0)
    public byte serverId;

    @JavaStruct.Field(order = 1, sizeof = "data")
    public short length;

    @JavaStruct.Field(order = 2)
    public T[] data;
}

// 这里的input.data的类型会被擦除，所以需要手动指定类型
StructArray01Test<short> input = new StructArray01Test<>();
input.serverId = 0x01;

// 这里手动赋值被擦除的类型
input.data = new short[] { 0x01, 0x02, 0x03 };
```
## 示例

请参阅[测试用例](./src/test/java/io/github/sollyu/test/StructTest.java)。

## License

```
Copyright 2023 sollyu.com.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
