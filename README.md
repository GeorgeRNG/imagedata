# imagedata
Generates an .mcfuntion file to display ever bad apple frame, or some other black and white video you feed it.  
Seen [here](https://youtu.be/1aOsH4fxYeQhttps://youtu.be/1aOsH4fxYeQ)

- Use ffmpeg to get every frame `ffmpeg -i video frame%03d.bmp`
- put all the frames in a folder called `frames`, which is in the same folder as the jar. (it expects a name like `00x` or `0xx` or `xxx` or `xxxx` ect)
- run the jar, with the amount of frames you want to render as the argument (more than there is will work)
- move the main.mcfunction into a datapack as the tick function
- summon a text display with the tags "badapple" and "0" (the frame number)
- enjoy

You can contribute if you like, maybe add grey or colored values.
