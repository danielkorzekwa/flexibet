delete from runner\_state\_bet where runner\_state\_id in (select rsb.runner\_state\_id from runner\_state\_bet rsb, runner\_state rs, market\_details m where rsb.runner\_state\_id=rs.id and rs.market\_id=m.market\_id and m.market\_time<'2009-12-25 00:00:00' limit 200000);

delete from runner\_state\_last\_bet where runner\_state\_id in (select rsb.runner\_state\_id from runner\_state\_last\_bet rsb, runner\_state rs, market m where rsb.runner\_state\_id=rs.id and rs.market\_id=m.market\_id and m.market\_time<'2009-12-25 00:00:00' limit 1000);

delete from runner\_state where id in (select rs.id from runner\_state rs, market m where rs.market\_id=m.market\_id and m.market\_time<'2009-09-27 00:00:00' limit 10);