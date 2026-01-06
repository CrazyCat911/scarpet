// When player takes damage, increase tick speed
// !!! IMPORTANT !!! //
// This script requires g4mespeed in order to work.


// Stay loaded
__config() -> (
    {
        ['scope', 'global'],
        ['stay-loaded', true]
    }
);

SAVE_FILE = 'scriptspeed';
TPS_DESC = '^ Ticks per Second. The default speed is 20 tps.';

__on_start() -> (
    saved = read_file(SAVE_FILE, 'json');
    new_speed = if(
        saved != null && has(saved, 'speed'),
        get(saved, 'speed'),
        20
    );
    set_tps(new_speed);
    print(format([
        'g Speed is ',
        'b ' + _round_to_3dp(new_speed),
        'w  tps', TPS_DESC,
        'g .'
    ]));
);

set_tps(new_speed) -> (
    global_speed = new_speed;
    run('tps ' + global_speed);
);


_round_to_3dp(amount) -> (
    round(amount * 1000) / 1000
);

__on_player_takes_damage(player, amount, source, source_entity) -> (
    old_speed = global_speed;
    new_speed = global_speed + min(amount, query(player, 'health'));
    set_tps(new_speed);
    print(format([
        'g Speed is ',
        'b ' + _round_to_3dp(new_speed),
        'r  (+' + _round_to_3dp(abs(old_speed - new_speed)) + ')',
        'w  tps', TPS_DESC,
        'g , due to ',
        'w ' + query(player, 'name'),
        'g  taking damage from ',
        'w ' + if(source_entity != null, query(source_entity, 'name'), 'the world')
    ]));
);

__on_player_deals_damage(player, amount, entity) -> (
    old_speed = global_speed;
    new_speed = max(global_speed - amount, 1);
    set_tps(new_speed);
    print(format([
        'g Speed is ',
        'b ' + _round_to_3dp(new_speed),
        'e  (-' + _round_to_3dp(abs(old_speed - new_speed)) + ')',
        'w  tps', TPS_DESC,
        'g , due to ',
        'w ' + query(player, 'name'),
        'g  attacking ',
        'w ' + query(entity, 'name')
    ]));
);

__on_close() -> (
    write_file(SAVE_FILE, 'json', {
        ['speed', global_speed]
    });
);
