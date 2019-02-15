interface EntityVisitor<R> {

    R visit(Blacksmith blacksmith);

    R visit(MinerFull minerFull);

    R visit(MinerNotFull minerNotFull);

    R visit(Obstacle obstacle);

    R visit(Ore ore);

    R visit(OreBlob oreBlob);

    R visit(Quake quake);

    R visit(Vein vein);

    R visit(Portal portal);

    R visit(EnemyHealer enemyHealer);

    R visit (Wyvern wyvern);

    R visit (Ninja ninja);
}
