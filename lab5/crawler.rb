load "open-uri.rb"

Website = Class.new do
    def initialize(new_url)
        @url = new_url
        new_url =~ /(.*\.edu)/i 
        @domain = $1
        @broken = false
    end

    def read()
        # encoding tip via http://stackoverflow.com/a/9612050/2178152
        @content = open(@url).read.force_encoding("ISO-8859-1").encode("utf-8", replace: nil)
    end

    def getLinks()
        @links = content.scan(/<a.+?href="(.+?)"/i).flatten

        # only crawl if on the bowdoin.edu domain
        

        @links.each do |link|
            if link.class() == String
                # fix links that are relative, e.g. '/admissions'
                if link =~ /\/(.+)/
                    link = @domain + $1
                end

                # just kill the link if it starts with #
                @links.delete(link) if link.start_with?("#")

                # then trim links for '#' - some pages are using this to do in-page nav
                link = link.split(/#/)[0]

                # delete the link if it's already visited
                $sites.each do |site|
                    @links.delete(link) if site.url == link
                end

                # remove duplicates (multiple links from the same page, etc)
                @links.each do |otherlink|
                    @links.delete(link) if otherlink == link
                end

                # don't crawl pdf/media files because they have to be fully downloaded (slow) and don't have links
            else
                @links.delete(link)
            end
        end

        return @links
    end

    def isBlob()
        # do not attempt to crawl blobs (http://en.wikipedia.org/wiki/Binary_large_object)
        if @url =~ /(\.jpg|\.jpeg|\.png|\.gif|\.pdf|\.mov|\.mp3|\.mp4|\.m4v|\.flv)$/i
            return true
        else
            return false
        end 
    end

    def getOwner()
        if @url =~ /~(.+)\// || @url =~ /~(.+)$/
            return $1
        else
            return "unknown"
        end
    end

    attr_reader :url, :content, :domain
    attr_accessor :broken
end

$sites = []

def crawl(wb)
    begin
        wb.read()
        $sites.push(wb)
    rescue
        puts "broken url #{wb.url}"
        wb.broken = true
        $sites.push(wb)
        return false
    end

    wb.getLinks().each do |link|
        @new_site = Website.new(link)
        if @new_site.domain =~ /https?:\/\/(www\.)?bowdoin.edu/i && !@new_site.isBlob()
            puts "crawling #{@new_site.url}"
            crawl(@new_site)
        end
    end
end

# GOOOOO

@start = Website.new("http://www.bowdoin.edu")

crawl(@start)

@outFile = File.new("deadlinks.txt", "w")

broken = 0

$sites.each do |site|
    if site.broken
        @outFile.puts "#{site.url}: owner: #{site.getOwner()}"
        broken +=1
    end
end

puts "#{broken} broken sites output to deadlinks.txt."