load "open-uri.rb"
require 'mail'

# This script requires mikel's mail gem: https://github.com/mikel/mail

Website = Class.new do
    # lol RFC http://en.wikipedia.org/wiki/HTTP_referer#Origin_of_the_term_referer
    def initialize(new_url, referer)
        @referer = referer
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

        @selected_links = []

        @links.each do |link|
            # some empty hrefs turn up as class Nil
            if link.class() == String

                # fix links that are relative to the base, e.g. '/admissions'
                if link =~ /^\/(.*)/
                    link = @domain + "/" + $1
                end

                # fix even more messed up relative links (eg., '../../index.html')
                rel_depth = link.scan(/\.\.\//).size
                if rel_depth > 0
                    link = @domain + @referer.split("/")[-1*rel_depth].join("/") + "/" + link.split("/")[rel_depth..-1]
                    puts link
                end

                # deal with links containing (or worse, starting with) '#'
                if !link.start_with?("#")
                    link = link.split(/#/)[0]

                    # ditch the link if it's already visited
                    repeated = false
                    $sites.each do |site|
                        if site.url == link
                            repeated = true 
                            break
                        end
                    end
                    if !repeated

                        # remove duplicates (multiple links from the same page, etc)
                        if !@selected_links.include?(link)

                            # don't crawl pdf/media files because they have to be fully downloaded (slow) and don't have links
                            if !self.isBlob()

                                # only crawl if on the bowdoin.edu domain
                                if link =~ /https?:\/\/(www\.)?bowdoin.edu/i
                                    @selected_links << link
                                end
                            end
                        end
                    end
                end
            end
        end

        return @selected_links
    end

    def isBlob()
        # identify blobs (http://en.wikipedia.org/wiki/Binary_large_object)
        return true if @url =~ /(\.jpg|\.jpeg|\.png|\.gif|\.pdf|\.mov|\.mp3|\.mp4|\.m4v|\.flv)$/i
        return false
    end

    def getOwner()
        if @url =~ /~(.+)\// || @url =~ /~(.+)$/
            return $1
        else
            return "unknown"
        end
    end

    attr_reader :url, :content, :domain, :referer
    attr_accessor :broken
end

$sites = []

def crawl(website)
    begin
        website.read()
        $sites.push(website)
    rescue
        puts "broken url #{website.url}"
        website.broken = true
        $sites.push(website)
        return false
    end

    website.getLinks().each do |link|
        @new_site = Website.new(link, website.url)
        puts "crawling #{@new_site.url}"
        crawl(@new_site)
    end
end


def mail_owner(site)
    mail = Mail.new do
        from    "bjacobel@bowdoin.edu"
        to      "#{site.owner}@bowdoin.edu"
        subject "Issues detected on your page"
        body    "Hi, #{site.owner}. An automated bot detected that your link to #{site.link} from the page #{site.referer} is broken. Have a nice day!"
    end
    
    mail.delivery_method :sendmail

    # don't actually send it!!!
    # mail.deliver

    puts "Sent a friendly email to #{site.owner}"
end

# start recursing
# The referrer of the first page is... I dunno...Google? It doesn't much matter
@start = Website.new("http://www.bowdoin.edu", "http://www.google.com")

crawl(@start)

@outFile = File.new("deadlinks.txt", "w")

broken = 0

$sites.each do |site|
    if site.broken
        @outFile.puts site.url
        mail_owner(site)
        broken +=1
    end
end

puts "#{broken} broken links output to deadlinks.txt."