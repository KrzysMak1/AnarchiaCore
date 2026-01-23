/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.File
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.sql.Connection
 *  java.sql.DriverManager
 *  java.sql.PreparedStatement
 *  java.sql.ResultSet
 *  java.sql.SQLException
 *  java.sql.Statement
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Map
 *  java.util.Set
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.io.BukkitObjectInputStream
 *  org.bukkit.util.io.BukkitObjectOutputStream
 */
package me.anarchiacore.customitems.stormitemy.storage.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class A {
    private final Plugin C;
    private final String A;
    private Connection B;

    public A(Plugin javaPlugin) {
        this.C = javaPlugin;
        this.A = javaPlugin.getDataFolder().getAbsolutePath() + File.separator + "data.db";
        this.C();
    }

    private void C() {
        try {
            Class.forName((String)"org.sqlite.JDBC");
            File file = this.C.getDataFolder();
            if (!file.exists()) {
                file.mkdirs();
            }
            String string = "jdbc:sqlite:" + this.A;
            this.B = DriverManager.getConnection((String)string);
            try (Statement statement = this.B.createStatement();){
                statement.execute("CREATE TABLE IF NOT EXISTS plecak_inventory (player_uuid VARCHAR(36) PRIMARY KEY,inventory_data BLOB NOT NULL,last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                statement.execute("CREATE TABLE IF NOT EXISTS plecak_death_data (player_uuid VARCHAR(36) PRIMARY KEY,backpack_item BLOB NOT NULL,slot_number INT NOT NULL,death_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                statement.execute("CREATE TABLE IF NOT EXISTS plecak_drop_bag (player_uuid VARCHAR(36) PRIMARY KEY,drop_bag_data BLOB NOT NULL,created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                statement.execute("CREATE TABLE IF NOT EXISTS sakiewka_drop_bag (pouch_id VARCHAR(36) PRIMARY KEY,item_type VARCHAR(50) DEFAULT 'sakiewka',drop_bag_data BLOB NOT NULL,created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public void B(UUID uUID, Inventory inventory) {
        if (this.B == null) {
            return;
        }
        try {
            byte[] byArray = this.A(inventory);
            String string = "INSERT OR REPLACE INTO plecak_inventory (player_uuid, inventory_data) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                preparedStatement.setBytes(2, byArray);
                preparedStatement.executeUpdate();
            }
        }
        catch (IOException | SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Inventory C(UUID uUID) {
        if (this.B == null) {
            return null;
        }
        try {
            String string = "SELECT inventory_data FROM plecak_inventory WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery();){
                    if (!resultSet.next()) return null;
                    byte[] byArray = resultSet.getBytes("inventory_data");
                    Inventory inventory = this.A(byArray, "\u00a78Plecak Drakuli");
                    return inventory;
                }
            }
        }
        catch (IOException | ClassNotFoundException | SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void A(UUID uUID, ItemStack itemStack, int n2) {
        if (this.B == null) {
            return;
        }
        try {
            byte[] byArray = this.A(itemStack);
            String string = "INSERT OR REPLACE INTO plecak_death_data (player_uuid, backpack_item, slot_number) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                preparedStatement.setBytes(2, byArray);
                preparedStatement.setInt(3, n2);
                preparedStatement.executeUpdate();
            }
        }
        catch (IOException | SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Map<String, Object> D(UUID uUID) {
        if (this.B == null) {
            return null;
        }
        try {
            String string = "SELECT backpack_item, slot_number FROM plecak_death_data WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery();){
                    if (!resultSet.next()) return null;
                    byte[] byArray = resultSet.getBytes("backpack_item");
                    int n2 = resultSet.getInt("slot_number");
                    HashMap hashMap = new HashMap();
                    hashMap.put((Object)"item", (Object)this.A(byArray));
                    hashMap.put((Object)"slot", (Object)n2);
                    HashMap hashMap2 = hashMap;
                    return hashMap2;
                }
            }
        }
        catch (IOException | ClassNotFoundException | SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void B(UUID uUID) {
        if (this.B == null) {
            return;
        }
        try {
            String string = "DELETE FROM plecak_death_data WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }

    public void A(UUID uUID, Inventory inventory) {
        if (this.B == null) {
            return;
        }
        try {
            byte[] byArray = this.A(inventory);
            String string = "INSERT OR REPLACE INTO plecak_drop_bag (player_uuid, drop_bag_data) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                preparedStatement.setBytes(2, byArray);
                preparedStatement.executeUpdate();
            }
        }
        catch (IOException | SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Inventory A(UUID uUID) {
        if (this.B == null) {
            return null;
        }
        try {
            String string = "SELECT drop_bag_data FROM plecak_drop_bag WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery();){
                    if (!resultSet.next()) return null;
                    byte[] byArray = resultSet.getBytes("drop_bag_data");
                    Inventory inventory = this.B(byArray, "pouch");
                    return inventory;
                }
            }
        }
        catch (IOException | ClassNotFoundException | SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void E(UUID uUID) {
        if (this.B == null) {
            return;
        }
        try {
            String string = "DELETE FROM plecak_drop_bag WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);){
                preparedStatement.setString(1, uUID.toString());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }

    public void A(String string, Inventory inventory) {
        if (this.B == null) {
            return;
        }
        try {
            byte[] byArray = this.A(inventory);
            String string2 = "INSERT OR REPLACE INTO sakiewka_drop_bag (pouch_id, item_type, drop_bag_data) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                preparedStatement.setString(1, string);
                preparedStatement.setString(2, "sakiewka");
                preparedStatement.setBytes(3, byArray);
                preparedStatement.executeUpdate();
            }
        }
        catch (IOException | SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Inventory G(String string) {
        if (this.B == null) {
            return null;
        }
        try {
            String string2 = "SELECT drop_bag_data FROM sakiewka_drop_bag WHERE pouch_id = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                preparedStatement.setString(1, string);
                try (ResultSet resultSet = preparedStatement.executeQuery();){
                    if (!resultSet.next()) return null;
                    byte[] byArray = resultSet.getBytes("drop_bag_data");
                    Inventory inventory = this.B(byArray, "pouch");
                    return inventory;
                }
            }
        }
        catch (IOException | ClassNotFoundException | SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void E(String string) {
        if (this.B == null) {
            return;
        }
        try {
            String string2 = "DELETE FROM sakiewka_drop_bag WHERE pouch_id = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                preparedStatement.setString(1, string);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public boolean C(String string) {
        if (this.B == null) {
            return false;
        }
        try {
            String string2 = "SELECT 1 FROM sakiewka_drop_bag WHERE pouch_id = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                boolean bl;
                block15: {
                    preparedStatement.setString(1, string);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        bl = resultSet.next();
                        if (resultSet == null) break block15;
                    }
                    catch (Throwable throwable) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            }
                            catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    }
                    resultSet.close();
                }
                return bl;
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
            return false;
        }
    }

    public Set<String> A() {
        HashSet hashSet = new HashSet();
        if (this.B == null) {
            return hashSet;
        }
        try {
            String string = "SELECT pouch_id FROM sakiewka_drop_bag";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string);
                 ResultSet resultSet = preparedStatement.executeQuery();){
                while (resultSet.next()) {
                    hashSet.add((Object)resultSet.getString("pouch_id"));
                }
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        return hashSet;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Inventory A(String string) {
        if (this.B == null) {
            return null;
        }
        try {
            String string2 = "SELECT drop_bag_data FROM plecak_drop_bag WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                preparedStatement.setString(1, string);
                try (ResultSet resultSet = preparedStatement.executeQuery();){
                    if (!resultSet.next()) return null;
                    byte[] byArray = resultSet.getBytes("drop_bag_data");
                    Inventory inventory = this.B(byArray, "pouch");
                    return inventory;
                }
            }
        }
        catch (IOException | ClassNotFoundException | SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void B(String string) {
        if (this.B == null) {
            return;
        }
        try {
            String string2 = "DELETE FROM plecak_drop_bag WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                preparedStatement.setString(1, string);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public boolean H(String string) {
        if (this.B == null) {
            return false;
        }
        try {
            String string2 = "SELECT 1 FROM plecak_drop_bag WHERE player_uuid = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                boolean bl;
                block15: {
                    preparedStatement.setString(1, string);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        bl = resultSet.next();
                        if (resultSet == null) break block15;
                    }
                    catch (Throwable throwable) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            }
                            catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    }
                    resultSet.close();
                }
                return bl;
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
            return false;
        }
    }

    public void A(String string, String string2, Inventory inventory) {
        if (this.B == null) {
            return;
        }
        try {
            byte[] byArray = this.A(inventory);
            String string3 = "INSERT OR REPLACE INTO sakiewka_drop_bag (pouch_id, item_type, drop_bag_data) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string3);){
                preparedStatement.setString(1, string);
                preparedStatement.setString(2, string2);
                preparedStatement.setBytes(3, byArray);
                preparedStatement.executeUpdate();
            }
        }
        catch (IOException | SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Inventory F(String string) {
        if (this.B == null) {
            return null;
        }
        try {
            String string2 = "SELECT drop_bag_data FROM sakiewka_drop_bag WHERE pouch_id = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                preparedStatement.setString(1, string);
                try (ResultSet resultSet = preparedStatement.executeQuery();){
                    if (!resultSet.next()) return null;
                    byte[] byArray = resultSet.getBytes("drop_bag_data");
                    Inventory inventory = this.B(byArray, "pouch");
                    return inventory;
                }
            }
        }
        catch (IOException | ClassNotFoundException | SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void D(String string) {
        if (this.B == null) {
            return;
        }
        try {
            String string2 = "DELETE FROM sakiewka_drop_bag WHERE pouch_id = ?";
            try (PreparedStatement preparedStatement = this.B.prepareStatement(string2);){
                preparedStatement.setString(1, string);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }

    private byte[] A(Inventory inventory) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream((OutputStream)byteArrayOutputStream);){
            bukkitObjectOutputStream.writeInt(inventory.getSize());
            for (ItemStack itemStack : inventory.getContents()) {
                bukkitObjectOutputStream.writeObject((Object)itemStack);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private Inventory A(byte[] byArray, String string) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
        try (BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream((InputStream)byteArrayInputStream);){
            int n2 = bukkitObjectInputStream.readInt();
            Inventory inventory = Bukkit.createInventory(null, (InventoryType)InventoryType.HOPPER, (String)string);
            for (int i2 = 0; i2 < n2 && i2 < 5; ++i2) {
                ItemStack itemStack = (ItemStack)bukkitObjectInputStream.readObject();
                if (itemStack == null || itemStack.getType() == Material.AIR) continue;
                inventory.setItem(i2, itemStack);
            }
            Inventory inventory2 = inventory;
            return inventory2;
        }
    }

    private Inventory B(byte[] byArray, String string) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
        try (BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream((InputStream)byteArrayInputStream);){
            int n2;
            int n3 = n2 = bukkitObjectInputStream.readInt();
            n3 = n2 < 9 ? 9 : (n2 > 54 ? 54 : (n2 + 4) / 9 * 9);
            Inventory inventory = Bukkit.createInventory(null, (int)n3, (String)string);
            for (int i2 = 0; i2 < n2; ++i2) {
                ItemStack itemStack = (ItemStack)bukkitObjectInputStream.readObject();
                if (itemStack == null || itemStack.getType() == Material.AIR || i2 >= n3) continue;
                inventory.setItem(i2, itemStack);
            }
            Inventory inventory2 = inventory;
            return inventory2;
        }
    }

    private byte[] A(ItemStack itemStack) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream((OutputStream)byteArrayOutputStream);){
            bukkitObjectOutputStream.writeObject((Object)itemStack);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private ItemStack A(byte[] byArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
        try (BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream((InputStream)byteArrayInputStream);){
            ItemStack itemStack = (ItemStack)bukkitObjectInputStream.readObject();
            return itemStack;
        }
    }

    public void B() {
        try {
            if (this.B != null && !this.B.isClosed()) {
                this.B.close();
            }
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }
}

