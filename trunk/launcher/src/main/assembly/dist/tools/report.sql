#last week
select rs.*, md.* from runner_state rs join market_details md on(rs.market_id=md.market_id) where extract(epoch from (now()-md.market_time))/60/60/24 between -10 and 10 and rs.state_name!='noBets' order by market_time desc;

#group by stat
select rs.state_name, count(*) from runner_state rs join market_details md on(rs.market_id=md.market_id) where extract(epoch from (now()-md.market_time))/60/60/24 between -10 and 10 group by rs.state_name;


#last 7 days
select rs.state_name, count(*) from runner_state rs join market_details md on(rs.market_id=md.market_id) where extract(epoch from (now()-md.market_time))/60/60/24 between 0 and 7 group by rs.state_name;

# in-play/future markets
select rs.state_name, count(*) from runner_state rs join market_details md on(rs.market_id=md.market_id) where extract(epoch from (now()-md.market_time))/60/60/24 between -100 and 0 group by rs.state_name;

# this week
select rs.state_name, count(*) from runner_state rs join market_details md on(rs.market_id=md.market_id) where md.market_time>'2008-11-10 00:00:00' and md.market_time<'2008-11-17 00:00:00'  and rs.state_name!='noBets' group by rs.state_name;
