package com.example.pfinal_pokezona_gabriel_jorge.data.repository

object GameRepository {
    private val gameCovers = mapOf(
        "red" to "https://upload.wikimedia.org/wikipedia/en/f/f4/Pok%C3%A9mon_Red_box_art.png",
        "blue" to "https://upload.wikimedia.org/wikipedia/en/a/a6/Pok%C3%A9mon_Blue_box_art.png",
        "yellow" to "https://upload.wikimedia.org/wikipedia/en/e/ea/Pok%C3%A9mon_Yellow_Cover_Art.png",
        "gold" to "https://upload.wikimedia.org/wikipedia/en/c/c8/Pok%C3%A9mon_Gold_box_art.png",
        "silver" to "https://upload.wikimedia.org/wikipedia/en/9/91/Pok%C3%A9mon_Silver_box_art.png",
        "crystal" to "https://upload.wikimedia.org/wikipedia/en/3/36/Pok%C3%A9mon_Crystal_Box_Art.png",
        "ruby" to "https://upload.wikimedia.org/wikipedia/en/1/1a/Pok%C3%A9mon_Ruby_box_art.png",
        "sapphire" to "https://upload.wikimedia.org/wikipedia/en/e/e0/Pok%C3%A9mon_Sapphire_box_art.png",
        "emerald" to "https://upload.wikimedia.org/wikipedia/en/7/77/Pok%C3%A9mon_Emerald_box_art.png",
        "firered" to "https://upload.wikimedia.org/wikipedia/en/8/87/Pok%C3%A9mon_FireRed_box_art.png",
        "leafgreen" to "https://upload.wikimedia.org/wikipedia/en/c/cd/Pok%C3%A9mon_LeafGreen_box_art.png",
        "diamond" to "https://upload.wikimedia.org/wikipedia/en/3/3d/Pok%C3%A9mon_Diamond_box_art.png",
        "pearl" to "https://upload.wikimedia.org/wikipedia/en/0/07/Pok%C3%A9mon_Pearl_box_art.png",
        "platinum" to "https://upload.wikimedia.org/wikipedia/en/b/bf/Pok%C3%A9mon_Platinum_box_art.png",
        "heartgold" to "https://upload.wikimedia.org/wikipedia/en/9/9f/Pok%C3%A9mon_HeartGold_box_art.png",
        "soulsilver" to "https://upload.wikimedia.org/wikipedia/en/6/62/Pok%C3%A9mon_SoulSilver_box_art.png",
        "black" to "https://upload.wikimedia.org/wikipedia/en/d/d7/Pok%C3%A9mon_Black_box_art.png",
        "white" to "https://upload.wikimedia.org/wikipedia/en/1/17/Pok%C3%A9mon_White_box_art.png",
        "black-2" to "https://upload.wikimedia.org/wikipedia/en/6/6e/Pok%C3%A9mon_Black_2_box_art.png",
        "white-2" to "https://upload.wikimedia.org/wikipedia/en/5/52/Pok%C3%A9mon_White_2_box_art.png",
        "x" to "https://upload.wikimedia.org/wikipedia/en/3/39/Pok%C3%A9mon_X_box_art.png",
        "y" to "https://upload.wikimedia.org/wikipedia/en/8/83/Pok%C3%A9mon_Y_box_art.png",
        "omega-ruby" to "https://upload.wikimedia.org/wikipedia/en/f/f6/Pok%C3%A9mon_Omega_Ruby_box_art.png",
        "alpha-sapphire" to "https://upload.wikimedia.org/wikipedia/en/1/1e/Pok%C3%A9mon_Alpha_Sapphire_box_art.png",
        "sun" to "https://upload.wikimedia.org/wikipedia/en/8/80/Pok%C3%A9mon_Sun_box_art.png",
        "moon" to "https://upload.wikimedia.org/wikipedia/en/0/0a/Pok%C3%A9mon_Moon_box_art.png",
        "ultra-sun" to "https://upload.wikimedia.org/wikipedia/en/9/90/Pok%C3%A9mon_Ultra_Sun_box_art.png",
        "ultra-moon" to "https://upload.wikimedia.org/wikipedia/en/f/f6/Pok%C3%A9mon_Ultra_Moon_box_art.png",
        "sword" to "https://upload.wikimedia.org/wikipedia/en/d/d9/Pok%C3%A9mon_Sword_box_art.png",
        "shield" to "https://upload.wikimedia.org/wikipedia/en/c/c1/Pok%C3%A9mon_Shield_box_art.png",
        "scarlet" to "https://upload.wikimedia.org/wikipedia/en/3/36/Pok%C3%A9mon_Scarlet_and_Violet_Scarlet_Box_Art.jpg",
        "violet" to "https://upload.wikimedia.org/wikipedia/en/7/73/Pok%C3%A9mon_Scarlet_and_Violet_Violet_Box_Art.jpg"
    )

    fun getCover(gameName: String): String {
        return gameCovers[gameName] ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png"
    }
}