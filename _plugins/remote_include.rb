require 'open-uri'

module Jekyll
  class RemoteIncludeTag < Liquid::Tag
    def initialize(tag_name, text, tokens)
      super
      @url = text.strip
    end

    def render(context)
      content = open(@url).read
      Liquid::Template.parse(content).render(context)
    end
  end
end

Liquid::Template.register_tag('remote_include', Jekyll::RemoteIncludeTag)
