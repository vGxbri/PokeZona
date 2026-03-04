package com.example.pfinal_pokezona_gabriel_jorge.data.repository

object GameRepository {
    private val gameCovers = mapOf(
        "red" to "https://static.wikia.nocookie.net/espokemon/images/6/6b/Car%C3%A1tula_Pok%C3%A9mon_Rojo_%28Jap%C3%B3n%29.png/revision/latest?cb=20151013131441",
        "blue" to "https://static.wikia.nocookie.net/espokemon/images/3/39/Car%C3%A1tula_Pok%C3%A9mon_Azul_%28Jap%C3%B3n%29.png/revision/latest?cb=20151013132356",
        "yellow" to "https://static.wikia.nocookie.net/espokemon/images/9/95/Pok%C3%A9mon_Amarillo.png/revision/latest?cb=20160715100157",
        "gold" to "https://static.wikia.nocookie.net/espokemon/images/6/6b/Pokemon_Edici%C3%B3n_Oro.jpg/revision/latest?cb=20160715092932",
        "silver" to "https://static.wikia.nocookie.net/espokemon/images/7/73/Pokemon_Edici%C3%B3n_Plata.jpg/revision/latest?cb=20160715093037",
        "crystal" to "https://images.wikidexcdn.net/mwuploads/wikidex/thumb/3/3b/latest/20160715093139/Pokemon_Edici%C3%B3n_Cristal.jpg/800px-Pokemon_Edici%C3%B3n_Cristal.jpg",
        "ruby" to "https://static.wikia.nocookie.net/espokemon/images/0/03/Car%C3%A1tula_de_Rub%C3%AD.png/revision/latest?cb=20110508145005",
        "sapphire" to "https://static.wikia.nocookie.net/espokemon/images/d/d3/Car%C3%A1tula_de_Zafiro.png/revision/latest?cb=20110508145255",
        "emerald" to "https://static.wikia.nocookie.net/espokemon/images/0/02/Caratula_Esmeralda.jpg/revision/latest?cb=20080314232122",
        "firered" to "https://static.wikia.nocookie.net/espokemon/images/a/ac/Car%C3%A1tula_de_Rojo_Fuego.png/revision/latest?cb=20190313061006",
        "leafgreen" to "https://static.wikia.nocookie.net/espokemon/images/3/31/Car%C3%A1tula_de_Verde_Hoja.png/revision/latest?cb=20140922113421",
        "diamond" to "https://static.wikia.nocookie.net/espokemon/images/9/94/Pok%C3%A9mon_Diamante.png/revision/latest?cb=20120517045308",
        "pearl" to "https://static.wikia.nocookie.net/espokemon/images/6/64/Pok%C3%A9mon_Perla.png/revision/latest?cb=20120517045246",
        "platinum" to "https://static.wikia.nocookie.net/espokemon/images/f/f4/Car%C3%A1tula_Pok%C3%A9mon_Platino_%28ESP%29.png/revision/latest?cb=20130710194938",
        "heartgold" to "https://m.media-amazon.com/images/I/81ESDoG1PVL._AC_UF894,1000_QL80_.jpg",
        "soulsilver" to "https://m.media-amazon.com/images/I/71WlibEzVaL.jpg",
        "black" to "https://static.wikia.nocookie.net/espokemon/images/a/ae/Portblacksp.jpg/revision/latest?cb=20120517045153",
        "white" to "https://static.wikia.nocookie.net/espokemon/images/9/95/Pkmnwhitesp.jpg/revision/latest?cb=20210615204455",
        "black-2" to "https://static.wikia.nocookie.net/espokemon/images/a/a4/Box_Pok%C3%A9mon_Negro_2.png/revision/latest/scale-to-width-down/1000?cb=20140217225209",
        "white-2" to "https://static.wikia.nocookie.net/espokemon/images/1/17/Box_Pok%C3%A9mon_Blanco_2.png/revision/latest/scale-to-width-down/1000?cb=20140218002530",
        "x" to "https://static.wikia.nocookie.net/espokemon/images/8/81/Pok%C3%A9mon_X_Car%C3%A1tula.png/revision/latest?cb=20130621162341",
        "y" to "https://static.wikia.nocookie.net/espokemon/images/6/6f/Pok%C3%A9mon_Y_Car%C3%A1tula.png/revision/latest?cb=20130621162348",
        "omega-ruby" to "https://static.wikia.nocookie.net/espokemon/images/5/5d/Car%C3%A1tula_Pok%C3%A9mon_Rub%C3%AD_Omega.png/revision/latest?cb=20150827182103",
        "alpha-sapphire" to "https://static.wikia.nocookie.net/espokemon/images/a/a6/Car%C3%A1tula_Pok%C3%A9mon_Zafiro_Alfa.png/revision/latest?cb=20150827181525",
        "sun" to "https://static.wikia.nocookie.net/espokemon/images/9/9b/Car%C3%A1tula_Pok%C3%A9mon_Sol.png/revision/latest?cb=20160906014708",
        "moon" to "https://static.wikia.nocookie.net/espokemon/images/7/73/Car%C3%A1tula_Pok%C3%A9mon_Luna.png/revision/latest?cb=20160906014824",
        "ultra-sun" to "https://images.wikidexcdn.net/mwuploads/wikidex/thumb/7/71/latest/20170823154238/Car%C3%A1tula_Pok%C3%A9mon_Ultrasol.png/800px-Car%C3%A1tula_Pok%C3%A9mon_Ultrasol.png",
        "ultra-moon" to "https://m.media-amazon.com/images/I/81mabwutpPL.jpg",
        "lets-go-pikachu" to "https://images.wikidexcdn.net/mwuploads/wikidex/thumb/b/b3/latest/20180530021112/Car%C3%A1tula_Pok%C3%A9mon_Let%27s_Go_Pikachu.png/800px-Car%C3%A1tula_Pok%C3%A9mon_Let%27s_Go_Pikachu.png",
        "lets-go-eevee" to "https://m.media-amazon.com/images/I/81JM6a1o-1L.jpg",
        "sword" to "https://m.media-amazon.com/images/I/81-QH2Lyy4L.jpg",
        "shield" to "https://m.media-amazon.com/images/I/81lwcYUU9TL.jpg",
        "brilliant-diamond" to "https://m.media-amazon.com/images/I/61PdJkQqx8L.jpg",
        "shining-pearl" to "https://m.media-amazon.com/images/I/71K2OlG5ulL.jpg",
        "legends-arceus" to "https://m.media-amazon.com/images/I/51IY5qMo-vL._AC_UF1000,1000_QL80_.jpg",
        "scarlet" to "https://m.media-amazon.com/images/I/81Nqi9GxAjL.jpg",
        "violet" to "https://m.media-amazon.com/images/I/81wwSWCghnL._AC_UF894,1000_QL80_.jpg",
        "legends-z-a" to "https://images.wikidexcdn.net/mwuploads/wikidex/thumb/e/e9/latest/20250528133349/Car%C3%A1tula_Leyendas_Pok%C3%A9mon_Z-A_%28Switch%29.png/800px-Car%C3%A1tula_Leyendas_Pok%C3%A9mon_Z-A_%28Switch%29.png"
    )

    fun getCover(gameName: String): String {
        return gameCovers[gameName] ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png"
    }
}