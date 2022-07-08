package com.survivingcodingbootcamp.blog.controller;

import com.survivingcodingbootcamp.blog.model.Hashtag;
import com.survivingcodingbootcamp.blog.model.Post;
import com.survivingcodingbootcamp.blog.repository.HashtagRepository;
import com.survivingcodingbootcamp.blog.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {
    private PostRepository postRepo;
    private HashtagRepository hashtagRepo;

    public PostController(PostRepository postRepo, HashtagRepository hashtagRepo) {
        this.postRepo = postRepo;
        this.hashtagRepo = hashtagRepo;
    }

    @GetMapping("/{id}")
    public String displaySinglePost(@PathVariable long id, Model model) {
        model.addAttribute("post", postRepo.findById(id).get());
        return "single-post-template";
    }

    @PostMapping("/{id}/addHashtag")
    public String addHashTagToSite(@PathVariable Long id, @RequestParam String hashtag) {
        Post post = postRepo.findById(id).get();
        if (hashtag.charAt(0)!='#') {
            hashtag = "#"+hashtag;
        }
        Optional<Hashtag> hashtagOptional = hashtagRepo.findByNameIgnoreCase(hashtag);
        if(hashtagOptional.isPresent()) {
            if(!post.getHashtags().contains(hashtagOptional.get())) {
                post.addHashtag(hashtagOptional.get());
            }
        } else {
            Hashtag hashtag15 = new Hashtag(hashtag);
            hashtagRepo.save(hashtag15);
            post.addHashtag(hashtag15);
        }
        postRepo.save(post);
        return "redirect:/posts/"+id;
    }

//    @PostMapping("/{id}/addPost")
//    public String addPostToTopic(@PathVariable Long id, @RequestParam String title,
//                                 String content, String author){
//        Post tempPost = new Post(title,topicRepo.findById(id).get(),content,author);
//        postRepo.save(tempPost);
//        tempPost.addPost(tempPost);
//    }
}
