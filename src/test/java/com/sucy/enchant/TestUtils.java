package com.sucy.enchant;

import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.vanilla.VanillaEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * EnchantmentAPI © 2017
 * com.sucy.enchant.TestUtils
 */
public class TestUtils {

    public static final EnchantmentAPI API = wrapAPI();

    public static Enchantment createVanillaEnchantment(final String name) {
        final VanillaEnchantment enchant = mock(VanillaEnchantment.class);
        when(enchant.getName()).thenReturn(name);
        API.register(enchant);

        final Enchantment enchantment = mock(Enchantment.class);
        when(enchantment.getName()).thenReturn(name);
        return enchantment;
    }

    public static CustomEnchantment createEnchantment(final String name) {
        final CustomEnchantment enchant = mock(CustomEnchantment.class);
        when(enchant.getName()).thenReturn(name);
        API.register(enchant);
        return enchant;
    }

    public static void tearDown() throws Exception {
        getRegistry().clear();
    }

    public static Map<String, CustomEnchantment> getRegistry() throws Exception {
        return EnchantmentAPI.getEnchantments();
    }

    public static void register(final CustomEnchantment enchantment) throws Exception {
        getRegistry().put(enchantment.getName(), enchantment);
    }

    private static EnchantmentAPI wrapAPI() {
        final EnchantmentAPI api = mock(EnchantmentAPI.class);
        doCallRealMethod().when(api).register(any());
        return api;
    }
}
