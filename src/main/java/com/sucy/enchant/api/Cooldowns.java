package com.sucy.enchant.api;

import org.bukkit.entity.LivingEntity;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

/**
 * EnchantmentAPI © 2017
 * com.sucy.enchant.api.Cooldowns
 * <p>
 * Helper class for managing cooldowns for enchantments
 */
public class Cooldowns {
    private static final String COOLDOWN = "cooldown";

    private static final Map<String, Long> COOLDOWNS = new HashMap<>();

    private static Clock clock = Clock.systemUTC();


    // visible for testing
    public static void setClock(Clock clock) {
        Cooldowns.clock = clock;
    }

    /**
     * Configures the cooldown for an enchantment
     *
     * @param settings settings of the enchantment
     * @param base     base value to configure
     * @param scale    scaling value to configure
     */
    public static void configure(final Settings settings, final double base, final double scale) {
        settings.set(COOLDOWN, base, scale);
    }

    /**
     * Computes the number of seconds left on the enchantment's cooldown,
     * rounded up to the nearest second.
     *
     * @param enchant  enchantment
     * @param user     player using the enchantment
     * @param settings settings of the enchantment
     * @param level    enchantment level
     * @return seconds left
     */
    public static int secondsLeft(final CustomEnchantment enchant, final LivingEntity user, final Settings settings, final int level) {
        final String key = makeKey(enchant, user);
        final long time = COOLDOWNS.getOrDefault(key, 0L);
        return (int) Math.ceil(settings.get(COOLDOWN, level) - (clock.millis() - time) / 1000.0);
    }

    /**
     * Checks whether or not the enchantment is on cooldown
     *
     * @param enchant  enchantment to check
     * @param user     player using the enchantment
     * @param settings settings of the enchantment
     * @param level    enchantment level
     * @return true if on cooldown, false otherwise
     */
    public static boolean onCooldown(final CustomEnchantment enchant, final LivingEntity user, final Settings settings, final int level) {
        return secondsLeft(enchant, user, settings, level) > 0;
    }

    /**
     * Starts the cooldown of an enchantment
     *
     * @param enchant enchantment to start for
     * @param user    player using the enchantment
     */
    public static void start(final CustomEnchantment enchant, final LivingEntity user) {
        final String key = makeKey(enchant, user);
        COOLDOWNS.put(key, clock.millis());
    }

    /**
     * Reduces the remaining time of an enchantment's cooldown.
     *
     * @param enchant enchantment to reduce the time for
     * @param user    player using the enchantment
     * @param millis  time to reduce by in milliseconds
     */
    public static void reduce(final CustomEnchantment enchant, final LivingEntity user, final long millis) {
        final String key = makeKey(enchant, user);
        COOLDOWNS.computeIfPresent(key, (k, time) -> time - millis);
    }

    private static String makeKey(final CustomEnchantment enchant, final LivingEntity user) {
        return enchant.getName() + user.getUniqueId();
    }
}
