# BTL_LapTrinhMang
- Note:
  - Every Request From Client Send to Server must have Action_Code
    => Depend on Action_Code, Server knows what actions user want to do
       Eg: `["Create Room", "Start Game", "Bar Move", ...]`
  - 1 Room ~ 1 WaitingRoomThread
