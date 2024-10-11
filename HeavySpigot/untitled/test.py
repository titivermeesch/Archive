import string

import journey


@journey.trigger(intent="random_joke")
def say_random_joke():
    joke = "This is a random joke"
    return journey.say_response('This is the joke of the day ' + joke)


@journey.stream_to(kafka_topic="your_moms_video")
def send_video_stream():
    return open_file_stream()  # Imagine this returns a filestream.

@journey.stream_from(kafka_topic="your_moms_video")
def send_video_stream(stream: filestream):
    stream.read() # Imagine this reads from the filestream to either play audio or smth.

@journey.trigger(intent="play_song")
def play_song(song_name: string):
    if not journey.store.contains_key('spotify_instance'):
        spotify_instance = SpotifyInstance()
        journey.store.put('spotify_instance', spotify_instance)
    else:
        spotify_instance = journey.store.get('spotify_instance')
    journey.say_response('Playing song: ' + song_name)
    spotify_instance.play(song_name)
    return journey.reset_context()

def stop_song():
    if not journey.store.contains_key('spotify_instance'):
        journey.say_response('no song is playing')
        return journey.new_context('spotify_choose_song')
    else:
        spotify_instance = journey.store.get('spotify_instance')
        spotify_instance.stop()
        return journey.reset_context()

def pause_song(pause: bool):
    if not journey.store.contains_key('spotify_instance'):
        journey.say_response('no song is playing')
        return journey.new_context('spotify_choose_song')
    else:
        spotify_instance = journey.store.get('spotify_instance')
        spotify_instance.pause(pause)
        return journey.reset_context()