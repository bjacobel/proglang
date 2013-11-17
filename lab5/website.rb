load "open-uri.rb"

Website = Class.new do
    def initialize(url)
        @url = url
        @broken = false

        if url =~ /~(.+)\// || url =~ /~(.+)$/
            @owner = $1
        end

        if url =~ /\.pdf$/
            @is_pdf = true
        else
            @is_pdf = false
        end 
    end

    attr_reader :url, :owner, :is_pdf
    attr_accessor :broken
end

w = Website.new("http://www.bowdoin.edu/")

begin
    wp = open(w.url).read.force_encoding("ISO-8859-1").encode("utf-8", replace: nil)
rescue
    raise $!
end

links = wp.scan(/<a.+?href="(.+?)"/i).flatten

links.each do |oneLink|
    oneLink.insert(0, w.url) if oneLink.start_with?("/")
end

links.delete_if {|oneLink| oneLink.start_with?("#")}
puts "Number of links: #{links.size}"

