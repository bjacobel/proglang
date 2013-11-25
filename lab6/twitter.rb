require 'twitter'

Twitter.configure do |config|
    config.consumer_key = "xaD9NI779fFhx5EpegCwbw"
    config.consumer_secret = "QiRYthjNH0dEMP1kBDTlVfuUKjRrYi90ZwH1B9t9Q"
    config.oauth_token = "15299776-qbzGy4hqag1xGVL4YEzaI0Pu27uqPvVEuDGslnN74"
    config.oauth_token_secret = "Yt78nOxoOcqgqgvAWKJ1BTjkfkhkiCUQ5vXiTShMTe6v5"
end

$users = [] 

def find_friends(user, depth)
    if depth == 2
        puts "Maximum depth reached."
        return
    else
        begin
            $friends = Twitter.friends(user)
        rescue Twitter::Error::TooManyRequests => error
            puts "Rate limit exceeded. Waiting #{error.rate_limit.reset_in} sec. ($users.length = #{$users.length})"
            sleep (error.rate_limit.reset_in)
            retry
        end

        $friends.each do |friend|
            if !$users.include?(friend.name)
                $users << friend.name
                puts "Adding #{friend.name}, and finding their friends..."
                begin
                    find_friends(friend, depth + 1)
                rescue Twitter::Error::TooManyRequests => error
                    puts "Rate limit exceeded. Waiting #{error.rate_limit.reset_in} sec. ($users.length = #{$users.length})"
                    sleep (error.rate_limit.reset_in)
                    retry
                end
            end
        end
    end
end

$users << "Ellis Ratner"
find_friends("ellisratner", 0)

puts "These are the #{$users.length} users reachable in 2 hops:"
puts $users

@outFile = File.new("users.txt", "w")
@outFile.puts($users)