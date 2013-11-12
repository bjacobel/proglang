load "open-uri.rb"

$visited = []
$broken = []

def crawl(url)
    begin
        # encoding tip via http://stackoverflow.com/a/9612050/2178152
        wp = open(url).read.force_encoding("ISO-8859-1").encode("utf-8", replace: nil)
    rescue
        puts "broken url #{url}"
        $broken.push(url)
        return false
    end

    $visited.push(url)
    url =~ /(.*\.edu)/i 
    domain = $1

    links = wp.scan(/<a.+?href="(.+?)"/i).flatten

    links.each do |link|
        # fix links that are relative, e.g. '/admissions'
        link.insert(0, domain) if link.start_with?("/")

        # trim links for '#' - some pages are using this to do in-page nav
        link = link.split(/#/)[0]

        if !$visited.include?(link) and !$broken.include?(link)
            # only crawl if on the bowdoin.edu domain
            # don't crawl pdf/media files because they have to be fully downloaded (slow) and don't have links
            if link =~ /https?:\/\/(www\.)?bowdoin.edu/i and link !~ /\.pdf\.mov|\.mp3|\.mp4|\.m4v|\.flv/i
                puts "crawling #{link}"
                crawl(link)
            end
        end
    end
end

# GOOOOO
crawl("http://www.bowdoin.edu")

outFile = File.new("deadlinks.txt", "w")

puts "Number of broken links found: #{$broken.size}"
puts "Links output to file deadlinks.txt."
outFile.puts $broken