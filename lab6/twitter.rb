require 'twitter'

Twitter.configure do |config|
    config.consumer_key = "xaD9NI779fFhx5EpegCwbw"
    config.consumer_secret = "QiRYthjNH0dEMP1kBDTlVfuUKjRrYi90ZwH1B9t9Q"
    config.oauth_token = "15299776-qbzGy4hqag1xGVL4YEzaI0Pu27uqPvVEuDGslnN74"
    config.oauth_token_secret = "Yt78nOxoOcqgqgvAWKJ1BTjkfkhkiCUQ5vXiTShMTe6v5"
end

$users = [] 

def find_friends(user, depth)
    if depth > 5
        puts "Maximum depth reached."
        return
    else
        new_friends = []
        
        begin
            Twitter.friends(user).each do |friend|
               new_friends << friend.name if !$users.include?(friend.name)
            end
        rescue Twitter::Error::TooManyRequests => error
            puts "Rate limit exceeded. Sleeping it off."
            puts "$users currently has #{$users.length} entries."
            sleep (error.rate_limit.reset_in + 60)
            retry
        end

        $users << new_friends
        puts "#{new_friends.length} nodes added to tree"

        new_friends.each do |friend| 
            find_friends(friend, depth + 1)
        end
    end
end

firstuser = "bjacobel"
$users << firstuser
find_friends(firstuser, 0)

puts "These are the #{$users.length} users reachable in 5 hops from #{firstuser}:"
puts $users