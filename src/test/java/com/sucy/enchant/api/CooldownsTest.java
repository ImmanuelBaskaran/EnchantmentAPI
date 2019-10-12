package com.sucy.enchant.api;

import org.bukkit.entity.LivingEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Clock;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * EnchantmentAPI © 2017
 * com.sucy.enchant.api.CooldownsTest
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CooldownsTest {

    private static final String ENCHANT_NAME = "enchantName";
    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    private Clock clock;
    @Mock
    private CustomEnchantment enchantment;
    @Mock
    private LivingEntity user;
    @Mock
    private Settings settings;

    @BeforeEach
    public void setUp() throws Exception {
        Cooldowns.setClock(clock);
        when(enchantment.getName()).thenReturn(ENCHANT_NAME);
        when(user.getUniqueId()).thenReturn(USER_ID);
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void configure() throws Exception {
        Cooldowns.configure(settings, 12, 23);

        verify(settings).set("cooldown", 12, 23);
    }

    @Test
    public void secondsLeft() throws Exception {
        when(clock.millis()).thenReturn(100L, 1100L);
        when(settings.get("cooldown", 2)).thenReturn(15.0);

        Cooldowns.start(enchantment, user);
        final int result = Cooldowns.secondsLeft(enchantment, user, settings, 2);
        assertEquals(result, 14);
    }

    @Test
    public void secondsLeft_roundedUp() throws Exception {
        when(clock.millis()).thenReturn(100L, 1000L);
        when(settings.get("cooldown", 2)).thenReturn(1.0);

        Cooldowns.start(enchantment, user);
        final int result = Cooldowns.secondsLeft(enchantment, user, settings, 2);
        assertEquals(result, 1);
    }

    @Test
    public void onCooldown() throws Exception {
        when(clock.millis()).thenReturn(100L, 1100L);
        when(settings.get("cooldown", 2)).thenReturn(15.0);

        Cooldowns.start(enchantment, user);
        final boolean result = Cooldowns.onCooldown(enchantment, user, settings, 2);
        assertTrue(result);
    }

    @Test
    public void onCooldown_exact() throws Exception {
        when(clock.millis()).thenReturn(100L, 15100L);
        when(settings.get("cooldown", 2)).thenReturn(15.0);

        Cooldowns.start(enchantment, user);
        final boolean result = Cooldowns.onCooldown(enchantment, user, settings, 2);
        assertFalse(result);
    }

    @Test
    public void onCooldown_offCooldown() throws Exception {
        when(clock.millis()).thenReturn(100L, 20100L);
        when(settings.get("cooldown", 2)).thenReturn(15.0);

        Cooldowns.start(enchantment, user);
        final boolean result = Cooldowns.onCooldown(enchantment, user, settings, 2);
        assertFalse(result);
    }

    @Test
    public void reduce() throws Exception {
        when(clock.millis()).thenReturn(100L, 10100L);
        when(settings.get("cooldown", 2)).thenReturn(15.0);

        Cooldowns.start(enchantment, user);
        Cooldowns.reduce(enchantment, user, 6000);
        final boolean result = Cooldowns.onCooldown(enchantment, user, settings, 2);
        assertFalse(result);
    }

    @Test
    public void reduceNegative() throws Exception {
        when(clock.millis()).thenReturn(100L, 20100L);
        when(settings.get("cooldown", 2)).thenReturn(15.0);

        Cooldowns.start(enchantment, user);
        Cooldowns.reduce(enchantment, user, -6000);
        final boolean result = Cooldowns.onCooldown(enchantment, user, settings, 2);
        assertTrue(result);
    }
}
