"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import {
  ChevronLeft,
  ChevronRight,
  Plus,
  Search,
  Send,
  BarChart3,
  FileText,
  Lightbulb,
  Brain,
  Target,
  Zap,
  BookOpen,
  MessageSquare,
  Settings,
  X,
  Copy,
  Trash2,
  Minus,
  Upload,
  Link,
  ArrowLeft,
} from "lucide-react"

interface StudioCard {
  id: string
  title: string
  icon: React.ReactNode
  description: string
}

const studioCards: StudioCard[] = [
  { id: "audio-overview", title: "音频概览", icon: <BarChart3 className="w-5 h-5" />, description: "分析音频内容概览" },
  { id: "mind-map", title: "思维导图", icon: <Brain className="w-5 h-5" />, description: "生成思维导图" },
  { id: "report", title: "报告", icon: <FileText className="w-5 h-5" />, description: "生成详细报告" },
  { id: "flashcard", title: "智能闪卡", icon: <Zap className="w-5 h-5" />, description: "创建学习闪卡" },
  { id: "test", title: "测验", icon: <Target className="w-5 h-5" />, description: "生成测验题目" },
  { id: "briefing", title: "简报文档", icon: <BookOpen className="w-5 h-5" />, description: "创建简报文档" },
  { id: "smart-test", title: "智能测验", icon: <Lightbulb className="w-5 h-5" />, description: "智能化测验系统" },
  { id: "smart-card", title: "智能闪卡", icon: <MessageSquare className="w-5 h-5" />, description: "智能闪卡系统" },
]

const flashcardQuestions = [
  "根据挑顺雨的观点，人工智能的下半场主要关注什么？",
  "AI发展的下半场将重点解决哪些问题？",
  "强化学习在AI发展中扮演什么角色？",
]

export default function AIInterface() {
  const [isSourcesCollapsed, setIsSourcesCollapsed] = useState(false)
  const [isStudioCollapsed, setIsStudioCollapsed] = useState(false)
  const [selectedCard, setSelectedCard] = useState(null)
  const [currentCardIndex, setCurrentCardIndex] = useState(0)
  const [inputValue, setInputValue] = useState("")
  const [expandedStudioContent, setExpandedStudioContent] = useState(null)
  const [showMindMap, setShowMindMap] = useState(false)
  const [mindMapZoom, setMindMapZoom] = useState(1)
  const [expandedNodes, setExpandedNodes] = useState({
    core: true,
    milestones: true,
    innovations: true,
    methods: true,
  })
  const [showAddSourceModal, setShowAddSourceModal] = useState(false)
  const [showPasteTextModal, setShowPasteTextModal] = useState(false)
  const [pasteTextValue, setPasteTextValue] = useState("")
  const [expandedSourceContent, setExpandedSourceContent] = useState(null)
  const [isSourceGuideCollapsed, setIsSourceGuideCollapsed] = useState(false)

  const handleCardClick = (card) => {
    if (card.id === "flashcard") {
      setExpandedStudioContent("flashcard")
      setCurrentCardIndex(0)
      setIsStudioCollapsed(false)
    } else if (card.id === "mind-map") {
      setShowMindMap(true)
    } else {
      setSelectedCard(card)
      setCurrentCardIndex(0)
    }
  }

  const handleCollapsedStudioClick = (card) => {
    if (card.id === "flashcard") {
      setExpandedStudioContent("flashcard")
      setCurrentCardIndex(0)
      setIsStudioCollapsed(false)
    } else if (card.id === "mind-map") {
      setShowMindMap(true)
    }
  }

  const handleCloseExpandedContent = () => {
    setExpandedStudioContent(null)
  }

  const nextCard = () => {
    if (currentCardIndex < flashcardQuestions.length - 1) {
      setCurrentCardIndex(currentCardIndex + 1)
    }
  }

  const prevCard = () => {
    if (currentCardIndex > 0) {
      setCurrentCardIndex(currentCardIndex - 1)
    }
  }

  const toggleNode = (nodeId) => {
    setExpandedNodes((prev) => ({
      ...prev,
      [nodeId]: !prev[nodeId],
    }))
  }

  const zoomIn = () => {
    setMindMapZoom((prev) => Math.min(prev + 0.2, 2))
  }

  const zoomOut = () => {
    setMindMapZoom((prev) => Math.max(prev - 0.2, 0.5))
  }

  const handleBriefingDocumentClick = () => {
    setExpandedStudioContent("briefing")
  }

  const handleAddSourceClick = () => {
    setShowAddSourceModal(true)
  }

  const handlePasteTextClick = () => {
    setShowAddSourceModal(false)
    setShowPasteTextModal(true)
  }

  const handleInsertText = () => {
    // Handle text insertion logic here
    console.log("[v0] Inserting text:", pasteTextValue)
    setPasteTextValue("")
    setShowPasteTextModal(false)
  }

  const handleClosePasteTextModal = () => {
    setShowPasteTextModal(false)
    setShowAddSourceModal(true)
  }

  const handleSourceDocumentClick = () => {
    setExpandedSourceContent("shunyu-yao")
    setIsSourcesCollapsed(false)
  }

  const handleCloseExpandedSource = () => {
    setExpandedSourceContent(null)
  }

  const handleSourcesCollapseClick = () => {
    if (expandedSourceContent) {
      setExpandedSourceContent(null)
    } else {
      setIsSourcesCollapsed(!isSourcesCollapsed)
    }
  }

  return (
    <div className="h-screen text-foreground flex flex-col dark" style={{ backgroundColor: "rgb(55, 56, 59)" }}>
      {/* Header */}
      <header className="flex items-center justify-between p-4 border-b border-border">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 bg-gradient-to-br from-orange-500 to-red-500 rounded-full flex items-center justify-center">
            <span className="text-white text-sm font-bold">AI</span>
          </div>
          <h1 className="text-lg font-medium">AI's Second Half: Defining the Problems</h1>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="ghost" size="sm">
            <Settings className="w-4 h-4" />
            分享
          </Button>
          <Button variant="ghost" size="sm">
            <Settings className="w-4 h-4" />
            设置
          </Button>
          <Button variant="ghost" size="sm">
            <Settings className="w-4 h-4" />
          </Button>
          <Button variant="ghost" size="sm">
            <Settings className="w-4 h-4" />
          </Button>
        </div>
      </header>

      <div className="flex flex-1 overflow-hidden gap-2 p-2">
        {/* Sources Panel */}
        <div
          className={`${isSourcesCollapsed ? "w-12" : expandedSourceContent ? "w-[600px]" : "w-80"} transition-all duration-300 border border-border rounded-lg flex flex-col`}
          style={{ backgroundColor: "rgb(35, 38, 43)" }}
        >
          <div className="flex items-center justify-between p-4 border-b border-border">
            {!isSourcesCollapsed && !expandedSourceContent && <h2 className="font-medium">来源</h2>}
            {expandedSourceContent && (
              <div className="flex items-center gap-2 flex-1">
                <h2 className="font-medium">The Second Half – Shunyu Yao – 姚顺雨</h2>
                <Button variant="ghost" size="sm">
                  <FileText className="w-4 h-4" />
                </Button>
              </div>
            )}
            <div className="flex items-center gap-2">
              {expandedSourceContent && (
                <Button variant="ghost" size="sm" onClick={handleCloseExpandedSource}>
                  <X className="w-4 h-4" />
                </Button>
              )}
              <Button variant="ghost" size="sm" onClick={handleSourcesCollapseClick} className="ml-auto">
                {isSourcesCollapsed ? <ChevronRight className="w-4 h-4" /> : <ChevronLeft className="w-4 h-4" />}
              </Button>
            </div>
          </div>

          {isSourcesCollapsed ? (
            <div className="flex-1 p-2">
              <div className="space-y-3">
                <Button
                  variant="ghost"
                  size="sm"
                  className="w-full h-10 p-0 hover:bg-accent"
                  title="添加来源"
                  onClick={handleAddSourceClick}
                >
                  <Plus className="w-4 h-4" />
                </Button>
                <Button variant="ghost" size="sm" className="w-full h-10 p-0 hover:bg-accent" title="搜索来源">
                  <Search className="w-4 h-4" />
                </Button>
                <div className="border-t border-border pt-3">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="w-full h-10 p-0 hover:bg-accent"
                    title="The Second Half – Shunyu Yao – 姚顺雨"
                  >
                    <div className="w-4 h-4 bg-blue-500 rounded-sm"></div>
                  </Button>
                </div>
              </div>
            </div>
          ) : expandedSourceContent ? (
            <div className="flex-1 overflow-y-auto">
              {/* Source Guide Section */}
              <div className="border-b border-border" style={{ backgroundColor: "rgb(46, 51, 73)" }}>
                <div
                  className="flex items-center justify-between p-4 cursor-pointer hover:bg-accent/50"
                  onClick={() => setIsSourceGuideCollapsed(!isSourceGuideCollapsed)}
                >
                  <div className="flex items-center gap-2">
                    <div className="w-4 h-4 bg-blue-500 rounded-sm"></div>
                    <span className="font-medium">来源指南</span>
                  </div>
                  <Button variant="ghost" size="sm">
                    {isSourceGuideCollapsed ? (
                      <ChevronRight className="w-4 h-4" />
                    ) : (
                      <ChevronLeft className="w-4 h-4" />
                    )}
                  </Button>
                </div>

                {!isSourceGuideCollapsed && (
                  <div className="px-6 pb-4">
                    <p className="text-sm text-muted-foreground leading-relaxed mb-4">
                      文章探讨人工智能（AI）发展划分为"上半场"和"下半场"，指出我们正处于AI的中场休息阶段。AI的上半场主要聚焦于开发新的训练方法和模型，如DeepBlue和GPT-4，通过在既定基准上取得突破来衡量成功；在这一阶段，方法和模型的重要性占主导地位。然而，随着强化学习（RL）最终有效"这一通用配方的出现——它结合了大规模语言预训练，扩展和推理能力，使得解决各类复杂任务变得标准化且高效——AI的上半场将从解决问题转向定义问题，强调评估的根本性重新思考，以确保AI在现实世界中的实际应用，即效用问题。这意味着需要更加接近真实世界界面的评估标准，从而创造研究人员从发明新方法转向产品经理的创新方法。
                    </p>
                    <div className="flex flex-wrap gap-2">
                      <span className="px-3 py-1 bg-slate-700 text-white text-xs rounded-full">人工智能...</span>
                      <span className="px-3 py-1 bg-slate-700 text-white text-xs rounded-full">RL算法...</span>
                      <span className="px-3 py-1 bg-slate-700 text-white text-xs rounded-full">评估方法...</span>
                      <span className="px-3 py-1 bg-slate-700 text-white text-xs rounded-full">实用性...</span>
                      <span className="px-3 py-1 bg-slate-700 text-white text-xs rounded-full">新的...</span>
                    </div>
                  </div>
                )}
              </div>

              {/* Author Section */}
              <div className="p-4 border-b border-border">
                <h3 className="font-semibold mb-1">Shunyu Yao</h3>
                <p className="text-sm text-muted-foreground">姚顺雨</p>
              </div>

              {/* Document Content */}
              <div className="p-4">
                <h3 className="text-lg font-semibold mb-4">The Second Half</h3>
                <p className="text-sm text-muted-foreground mb-2">tldr: We're at AI's halftime.</p>

                <div className="space-y-4 text-sm leading-relaxed">
                  <p>
                    For decades, AI has largely been about developing new training methods and models. And it worked:
                    from beating world champions at chess and Go, surpassing most humans on the SAT and bar exams, to
                    earning IMO and IOI gold medals. Behind these milestones in the history book — DeepBlue, AlphaGo,
                    GPT-4, and the o-series — are fundamental innovations in AI methods: search, deep RL, scaling, and
                    reasoning. Things just get better over time.
                  </p>

                  <p>So what's suddenly different now?</p>

                  <p>
                    In three words: RL finally works. More precisely: RL finally generalizes. After several major
                    detours and a culmination of milestones, we've landed on a working recipe to solve a wide range of
                    RL tasks using language and reasoning. Even a year ago, if you told most AI researchers that a
                    single recipe could handle engineering, creative writing, IMO-level math, mouse-and-keyboard
                    manipulation, and long-form question answering — they'd laugh at your hallucinations. Each of these
                    tasks is incredibly difficult and many researchers spend their entire PhDs focused on just one
                    narrow slice.
                  </p>

                  <p>Yet it happened.</p>

                  <p>
                    So what comes next? The second half of AI — starting now — will shift focus from solving problems to
                    defining problems. In this new era, evaluation becomes more important than training. Instead of just
                    asking "Can we train a model to solve X?", we're asking, "What should we be training AI to do, and
                    how do we measure real progress?" To thrive in this second half, we'll need a timely shift in
                    mindset and skill set, ones perhaps closer to a product manager.
                  </p>
                </div>
              </div>
            </div>
          ) : (
            <div className="flex-1 p-4">
              <div className="flex gap-2 mb-4">
                <Button variant="outline" size="sm" className="flex-1 bg-transparent" onClick={handleAddSourceClick}>
                  <Plus className="w-4 h-4 mr-2" />
                  添加
                </Button>
                <Button variant="outline" size="sm" className="flex-1 bg-transparent">
                  <Search className="w-4 h-4 mr-2" />
                  搜索
                </Button>
              </div>

              <div className="space-y-2">
                <div className="flex items-center gap-2 p-2 rounded hover:bg-accent">
                  <input type="checkbox" className="rounded" />
                  <span className="text-sm">选择所有来源</span>
                </div>
                <div
                  className="flex items-center gap-2 p-2 rounded hover:bg-accent cursor-pointer"
                  onClick={handleSourceDocumentClick}
                >
                  <input type="checkbox" className="rounded" defaultChecked />
                  <div className="w-4 h-4 bg-blue-500 rounded-sm"></div>
                  <span className="text-sm">The Second Half – Shunyu Yao – 姚顺雨</span>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Main Content */}
        <div
          className={`${expandedStudioContent || expandedSourceContent ? "flex-1" : "flex-1"} flex flex-col transition-all duration-300 border border-border rounded-lg`}
          style={{ backgroundColor: "rgb(35, 38, 43)" }}
        >
          <div className="flex items-center justify-between p-4 border-b border-border">
            <h2 className="font-medium">对话</h2>
            <Button variant="ghost" size="sm">
              <Settings className="w-4 h-4" />
            </Button>
          </div>

          <div className="flex-1 p-6 overflow-y-auto">
            <div className="max-w-4xl mx-auto">
              <div className="flex items-center gap-3 mb-4">
                <div className="w-12 h-12 bg-gradient-to-br from-orange-500 to-red-500 rounded-full flex items-center justify-center">
                  <span className="text-white font-bold">AI</span>
                </div>
                <div>
                  <h3 className="text-xl font-semibold mb-2">AI's Second Half: Defining the Problems</h3>
                  <p className="text-sm text-muted-foreground">1 个来源</p>
                </div>
              </div>

              <div className="prose prose-invert max-w-none mb-8">
                <p className="text-foreground leading-relaxed">
                  这些摘录来自姚顺雨（Shunyu
                  Yao）的博客文章《下半场》，它描述出人工智能的发展正处于一个转折点。即所谓的"中场休息"。文章认为，人工智能发展的下半场主要关注于创造新的训练方法和模型，如Transformer和GPT系列，其衡量成功的方式是在既定基准上取得突破。然而，作者指出，由于强化学习（RL）配方的成熟和通用化（结合了大规模语言预训练），我们现在可以极其理解能力），重点测试的难点正在迁移解决方案，因此，人工智能的下半场将从解决问题转向定义问题，其重点在于重新思考等价论题，以确保人工智能系统能够解决现实世界的实用性问题，而不是仅仅在自动化、独立运行的学术基准上表现出色。
                </p>
              </div>

              <div className="flex items-center gap-2 mb-6">
                <Button variant="outline" size="sm">
                  <FileText className="w-4 h-4 mr-2" />
                  保存到笔记
                </Button>
                <Button variant="outline" size="sm">
                  <FileText className="w-4 h-4 mr-2" />
                  复制
                </Button>
              </div>

              <div className="grid grid-cols-3 gap-4 mb-8">
                <button className="px-6 py-3 rounded-full border border-white text-white bg-transparent hover:bg-white/10 transition-colors flex items-center justify-center gap-2">
                  <FileText className="w-5 h-5" />
                  <span className="font-medium">添加笔记</span>
                </button>
                <button className="px-6 py-3 rounded-full border border-white text-white bg-transparent hover:bg-white/10 transition-colors flex items-center justify-center gap-2">
                  <BarChart3 className="w-5 h-5" />
                  <span className="font-medium">音频概览</span>
                </button>
                <button className="px-6 py-3 rounded-full border border-white text-white bg-transparent hover:bg-white/10 transition-colors flex items-center justify-center gap-2">
                  <Brain className="w-5 h-5" />
                  <span className="font-medium">思维导图</span>
                </button>
              </div>
            </div>
          </div>

          {/* Input Area */}
          <div className="p-4 border-t border-border">
            <div className="max-w-4xl mx-auto">
              <div className="flex gap-2">
                <Input
                  placeholder="开始输入..."
                  value={inputValue}
                  onChange={(e) => setInputValue(e.target.value)}
                  className="flex-1"
                />
                <Button>
                  <Send className="w-4 h-4" />
                </Button>
              </div>
              <div className="flex items-center justify-between mt-2 text-sm text-muted-foreground">
                <span>人工智能下半场的问题新定义问题和评估标准？</span>
                <span>1 个来源</span>
              </div>
            </div>
          </div>
        </div>

        {/* Studio Panel */}
        <div
          className={`${isStudioCollapsed ? "w-12" : expandedStudioContent ? "w-[600px]" : "w-80"} transition-all duration-300 border border-border rounded-lg flex flex-col`}
          style={{ backgroundColor: "rgb(35, 38, 43)" }}
        >
          <div className="flex items-center justify-between p-4 border-b border-border">
            <Button variant="ghost" size="sm" onClick={() => setIsStudioCollapsed(!isStudioCollapsed)}>
              {isStudioCollapsed ? <ChevronLeft className="w-4 h-4" /> : <ChevronRight className="w-4 h-4" />}
            </Button>
            {!isStudioCollapsed && (
              <div className="flex items-center gap-2">
                {expandedStudioContent ? (
                  <div className="flex items-center gap-2">
                    <span className="font-medium">Studio</span>
                    <ChevronRight className="w-4 h-4" />
                    <span className="font-medium">{expandedStudioContent === "briefing" ? "举报" : "应用"}</span>
                  </div>
                ) : (
                  <h2 className="font-medium">Studio</h2>
                )}
                {expandedStudioContent && (
                  <Button variant="ghost" size="sm" onClick={handleCloseExpandedContent}>
                    <X className="w-4 h-4" />
                  </Button>
                )}
              </div>
            )}
          </div>

          {isStudioCollapsed ? (
            <div className="flex-1 p-2">
              <div className="space-y-2">
                {studioCards.map((card) => (
                  <Button
                    key={card.id}
                    variant="ghost"
                    size="sm"
                    className="w-full h-10 p-0 hover:bg-accent"
                    onClick={() => handleCollapsedStudioClick(card)}
                    title={card.title}
                  >
                    {card.icon}
                  </Button>
                ))}
                <div className="border-t border-border pt-2 mt-4">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="w-full h-10 p-0 hover:bg-accent"
                    title="Briefing Document"
                    onClick={handleBriefingDocumentClick}
                  >
                    <FileText className="w-4 h-4" />
                  </Button>
                  <Button variant="ghost" size="sm" className="w-full h-10 p-0 hover:bg-accent" title="智能测验">
                    <Lightbulb className="w-4 h-4" />
                  </Button>
                  <Button variant="ghost" size="sm" className="w-full h-10 p-0 hover:bg-accent" title="添加笔记">
                    <Plus className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            </div>
          ) : expandedStudioContent === "flashcard" ? (
            <div className="flex-1 flex flex-col">
              <div className="p-6">
                <div className="flex items-center justify-between mb-6">
                  <div>
                    <h3 className="text-xl font-semibold mb-2">智能闪卡</h3>
                    <p className="text-sm text-muted-foreground">基于 1 个来源</p>
                  </div>
                  <Button variant="ghost" size="sm">
                    <Settings className="w-4 h-4" />
                  </Button>
                </div>

                <div className="text-center mb-4">
                  <p className="text-sm text-muted-foreground">按空格键可翻转卡片，按"←"/"→"可浏览卡片</p>
                </div>

                <div className="flex items-center justify-center mb-6 relative">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="absolute left-0 z-10 rounded-full w-10 h-10 bg-slate-700 hover:bg-slate-600"
                    onClick={prevCard}
                    disabled={currentCardIndex === 0}
                  >
                    <ChevronLeft className="w-5 h-5 text-white" />
                  </Button>

                  <Card className="w-80 h-64 bg-gradient-to-br from-slate-800 to-green-800 border-0 flex items-center justify-center p-6 mx-12">
                    <div className="text-center">
                      <p className="text-white text-lg leading-relaxed">
                        文章中提到了哪些作为人工智能上半场里程碑的系统?
                      </p>
                    </div>
                  </Card>

                  <Button
                    variant="ghost"
                    size="sm"
                    className="absolute right-0 z-10 rounded-full w-10 h-10 bg-slate-700 hover:bg-slate-600"
                    onClick={nextCard}
                    disabled={currentCardIndex === flashcardQuestions.length - 1}
                  >
                    <ChevronRight className="w-5 h-5 text-white" />
                  </Button>
                </div>

                <div className="text-center mb-6">
                  <div className="text-sm text-muted-foreground">查看答案</div>
                </div>
              </div>

              <div className="mt-auto p-6 pt-0">
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center gap-2">
                    <Button variant="ghost" size="sm">
                      <Settings className="w-4 h-4" />
                      重新开始
                    </Button>
                  </div>
                  <div className="text-sm text-muted-foreground">
                    {currentCardIndex + 1}/{flashcardQuestions.length} 张卡片
                  </div>
                </div>

                <div className="w-full bg-muted rounded-full h-2 mb-4">
                  <div
                    className="bg-primary h-2 rounded-full transition-all duration-300"
                    style={{ width: `${((currentCardIndex + 1) / flashcardQuestions.length) * 100}%` }}
                  />
                </div>

                <div className="flex justify-between">
                  <Button variant="ghost" size="sm">
                    <FileText className="w-4 h-4 mr-2" />
                    优质内容
                  </Button>
                  <Button variant="ghost" size="sm">
                    <MessageSquare className="w-4 h-4 mr-2" />
                    多质内容
                  </Button>
                </div>
              </div>
            </div>
          ) : expandedStudioContent === "briefing" ? (
            <div className="flex-1 overflow-y-auto">
              <div className="p-6">
                <div className="flex items-center justify-between mb-6">
                  <div>
                    <h3 className="text-xl font-semibold mb-2">Briefing Document: The Second Half of AI</h3>
                    <p className="text-sm text-muted-foreground">基于 1 个来源</p>
                  </div>
                  <div className="flex items-center gap-2">
                    <Button variant="ghost" size="sm">
                      <Copy className="w-4 h-4" />
                    </Button>
                    <Button variant="ghost" size="sm">
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </div>

                <div className="space-y-6">
                  <div>
                    <h4 className="text-lg font-semibold mb-3">Briefing Document: The Second Half of AI</h4>
                  </div>

                  <div>
                    <h5 className="text-base font-semibold mb-3">Executive Summary</h5>
                    <p className="text-sm leading-relaxed text-muted-foreground mb-4">
                      The field of Artificial Intelligence is undergoing a fundamental paradigm shift, moving from a
                      "first half" focused on developing novel training methods to a "second half" centered on defining
                      problems and creating new evaluation frameworks. This transition is driven by the emergence of a
                      powerful and generalizable "recipe" for solving a wide range of tasks, which combines massive
                      language pre-training, scale, and reasoning. This recipe has proven so effective at "hillclimbing"
                      existing benchmarks that the old game of creating slightly better models for specific tasks is
                      becoming obsolete.
                    </p>
                    <p className="text-sm leading-relaxed text-muted-foreground mb-4">
                      The central challenge of this new era is the "utility problem": despite AI achieving superhuman
                      performance on standardized tests and games (e.g., SAT, bar exams, Go, IMO), its impact on
                      real-world utility, as measured by economics and GDP, remains limited. The source argues this gap
                      exists because current evaluation setups (e.g., autonomous, independent task-solving) do not
                      reflect real-world conditions (e.g., continuous human interaction, sequential learning). The path
                      forward, therefore, is not just to create harder benchmarks, but to fundamentally question and
                      reinvent evaluation itself to better align with real-world utility. This shift redefines the work
                      of AI researchers, moving their skill set closer to that of a product manager, and sets the stage
                      for creating truly game-changing products and companies.
                    </p>
                  </div>

                  <div>
                    <h5 className="text-base font-semibold mb-3">The First Half: The Era of Methods and Models</h5>
                    <p className="text-sm leading-relaxed text-muted-foreground">
                      For decades, the primary focus of AI research was the development of new training methods and
                      models. The major breakthroughs and most impactful academic papers of this period centered on
                      innovations that trained better models.
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-4 mt-8 pt-6 border-t border-border">
                  <Button variant="outline" className="flex-1 bg-transparent">
                    <FileText className="w-4 h-4 mr-2" />
                    报告内容不错
                  </Button>
                  <Button variant="outline" className="flex-1 bg-transparent">
                    <MessageSquare className="w-4 h-4 mr-2" />
                    报告内容不好
                  </Button>
                </div>
              </div>
            </div>
          ) : (
            <div className="flex-1 p-4">
              <div className="grid grid-cols-2 gap-3">
                {studioCards.map((card, index) => {
                  const cardColors = [
                    "bg-slate-700", // 音频概览
                    "bg-slate-700", // 思维导图
                    "bg-slate-700", // 报告
                    "bg-slate-700", // 智能闪卡
                    "bg-slate-700", // 测验
                    "bg-slate-700", // 简报文档
                    "bg-slate-700", // 智能测验
                    "bg-slate-700", // 智能闪卡
                  ]
                  return (
                    <div
                      key={card.id}
                      className={`p-3 ${cardColors[index]} hover:bg-slate-600 cursor-pointer transition-colors rounded-lg`}
                      onClick={() => handleCardClick(card)}
                    >
                      <div className="flex flex-col items-center text-center gap-2">
                        <div className="text-white">{card.icon}</div>
                        <span className="text-sm font-medium text-white">{card.title}</span>
                      </div>
                    </div>
                  )
                })}
              </div>

              <div className="mt-6 space-y-3">
                <div
                  className="flex items-center gap-3 p-3 rounded-lg hover:bg-accent/50 cursor-pointer transition-colors"
                  onClick={handleBriefingDocumentClick}
                >
                  <FileText className="w-5 h-5 text-muted-foreground" />
                  <div className="flex-1">
                    <div className="font-medium text-sm">Briefing Document: The Second Half of AI</div>
                    <div className="text-xs text-muted-foreground">Briefing Doc • 1 个来源 • 2 小时前</div>
                  </div>
                  <Button variant="ghost" size="sm">
                    <Settings className="w-4 h-4" />
                  </Button>
                </div>

                <div className="flex items-center gap-3 p-3 rounded-lg hover:bg-accent/50 transition-colors">
                  <Lightbulb className="w-5 h-5 text-muted-foreground" />
                  <div className="flex-1">
                    <div className="font-medium text-sm">智能测验</div>
                    <div className="text-xs text-muted-foreground">1 个来源 • 2 小时前</div>
                  </div>
                  <Button variant="ghost" size="sm">
                    <Settings className="w-4 h-4" />
                  </Button>
                </div>
              </div>

              <Button className="w-full mt-4 bg-transparent" variant="outline">
                <Plus className="w-4 h-4 mr-2" />
                添加笔记
              </Button>
            </div>
          )}
        </div>
      </div>

      {/* Mind Map Modal */}
      {showMindMap && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm">
          <div
            className="absolute inset-5 rounded-lg border border-border overflow-hidden"
            style={{ backgroundColor: "rgb(35, 38, 43)" }}
          >
            {/* Header */}
            <div className="flex items-center justify-between p-4 border-b border-border">
              <div>
                <h2 className="text-lg font-semibold">AI 的下半场: 范式与实用性</h2>
                <p className="text-sm text-muted-foreground">基于 1 个来源</p>
              </div>
              <div className="flex items-center gap-2">
                <Button variant="ghost" size="sm">
                  <Plus className="w-4 h-4" />
                </Button>
                <Button variant="ghost" size="sm" onClick={() => setShowMindMap(false)}>
                  <X className="w-4 h-4" />
                </Button>
              </div>
            </div>

            {/* Mind Map Content */}
            <div className="flex-1 relative overflow-hidden">
              <div
                className="absolute inset-0 flex items-center justify-center"
                style={{ transform: `scale(${mindMapZoom})` }}
              >
                <div className="relative w-full h-full flex items-center justify-center">
                  {/* Central Node */}
                  <div className="absolute bg-slate-700 text-white px-6 py-3 rounded-lg border border-slate-600 z-10">
                    <div className="text-center">
                      <div className="font-medium">AI工作流程: 配方 (The AI Working Recipe)</div>
                    </div>
                  </div>

                  {/* Left Node */}
                  <div className="absolute -left-80 bg-slate-700 text-white px-4 py-2 rounded-lg border border-slate-600">
                    <div className="flex items-center gap-2">
                      <button onClick={() => toggleNode("left")}>
                        <ChevronLeft className="w-4 h-4" />
                      </button>
                      <span className="text-sm">AI的下半场 (The Second Half of AI)</span>
                    </div>
                  </div>

                  {/* Top Node */}
                  <div className="absolute -top-20 bg-slate-700 text-white px-4 py-2 rounded-lg border border-slate-600">
                    <div className="flex items-center gap-2">
                      <span className="text-sm">
                        第一阶段: 关注训练方法 (The First Half: Focus on Training Methods)
                      </span>
                      <button onClick={() => toggleNode("top")}>
                        <ChevronLeft className="w-4 h-4" />
                      </button>
                    </div>
                  </div>

                  {/* Bottom Node */}
                  <div className="absolute top-20 bg-slate-700 text-white px-4 py-2 rounded-lg border border-slate-600">
                    <div className="flex items-center gap-2">
                      <span className="text-sm">
                        第二阶段: 关注问题定义 (The Second Half: Focus on Problem Definition)
                      </span>
                      <button onClick={() => toggleNode("bottom")}>
                        <ChevronRight className="w-4 h-4" />
                      </button>
                    </div>
                  </div>

                  {/* Right Side Nodes */}
                  <div className="absolute right-0 top-0 space-y-4">
                    {/* Core Focus */}
                    <div className="bg-slate-700 text-white px-4 py-2 rounded-lg border border-slate-600">
                      <div className="flex items-center gap-2">
                        <span className="text-sm">核心焦点 (Core Focus)</span>
                        <button onClick={() => toggleNode("core")}>
                          <ChevronRight className="w-4 h-4" />
                        </button>
                      </div>
                    </div>

                    {/* Milestones */}
                    <div className="bg-slate-700 text-white px-4 py-2 rounded-lg border border-slate-600">
                      <div className="flex items-center gap-2">
                        <span className="text-sm">成功标志 (Milestones)</span>
                        <button onClick={() => toggleNode("milestones")}>
                          <ChevronRight className="w-4 h-4" />
                        </button>
                      </div>
                    </div>

                    {/* Key Innovations */}
                    <div className="bg-slate-700 text-white px-4 py-2 rounded-lg border border-slate-600">
                      <div className="flex items-center gap-2">
                        <span className="text-sm">关键创新 (Key Innovations)</span>
                        <button onClick={() => toggleNode("innovations")}>
                          <ChevronRight className="w-4 h-4" />
                        </button>
                      </div>
                    </div>

                    {/* Method vs Benchmark */}
                    <div className="bg-slate-700 text-white px-4 py-2 rounded-lg border border-slate-600">
                      <div className="flex items-center gap-2">
                        <span className="text-sm">方法 vs. 基准 (Method vs. Benchmark)</span>
                        <button onClick={() => toggleNode("methods")}>
                          <ChevronRight className="w-4 h-4" />
                        </button>
                      </div>
                    </div>
                  </div>

                  {/* Connection Lines */}
                  <svg className="absolute inset-0 w-full h-full pointer-events-none" style={{ zIndex: 1 }}>
                    {/* Central to Left */}
                    <line x1="50%" y1="50%" x2="30%" y2="50%" stroke="#64748b" strokeWidth="2" />
                    {/* Central to Top */}
                    <line x1="50%" y1="50%" x2="50%" y2="40%" stroke="#64748b" strokeWidth="2" />
                    {/* Central to Bottom */}
                    <line x1="50%" y1="50%" x2="50%" y2="60%" stroke="#64748b" strokeWidth="2" />
                    {/* Central to Right nodes */}
                    <line x1="50%" y1="50%" x2="70%" y2="45%" stroke="#64748b" strokeWidth="2" />
                    <line x1="50%" y1="50%" x2="70%" y2="50%" stroke="#64748b" strokeWidth="2" />
                    <line x1="50%" y1="50%" x2="70%" y2="55%" stroke="#64748b" strokeWidth="2" />
                    <line x1="50%" y1="50%" x2="70%" y2="60%" stroke="#64748b" strokeWidth="2" />
                  </svg>
                </div>
              </div>

              {/* Zoom Controls */}
              <div className="absolute bottom-4 right-4 flex flex-col gap-2">
                <Button variant="outline" size="sm" className="w-10 h-10 p-0 bg-card" onClick={zoomIn}>
                  <Plus className="w-4 h-4" />
                </Button>
                <Button variant="outline" size="sm" className="w-10 h-10 p-0 bg-card" onClick={zoomOut}>
                  <Minus className="w-4 h-4" />
                </Button>
              </div>
            </div>

            {/* Footer */}
            <div className="flex items-center justify-between p-4 border-t border-border">
              <div className="flex items-center gap-4">
                <Button variant="outline" size="sm">
                  <FileText className="w-4 h-4 mr-2" />
                  优质内容
                </Button>
                <Button variant="outline" size="sm">
                  <MessageSquare className="w-4 h-4 mr-2" />
                  劣质内容
                </Button>
              </div>
              <div className="text-sm text-muted-foreground">
                NotebookLM 提供的内容必须验证，因此请仔细检查重要内容。
              </div>
            </div>
          </div>
        </div>
      )}

      {showAddSourceModal && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm">
          <div className="absolute inset-0 flex items-center justify-center p-4">
            <div
              className="rounded-lg border border-border w-full max-w-4xl max-h-[90vh] overflow-hidden"
              style={{ backgroundColor: "rgb(35, 38, 43)" }}
            >
              {/* Header */}
              <div className="flex items-center justify-between p-6 border-b border-border">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 bg-gradient-to-br from-orange-500 to-red-500 rounded-full flex items-center justify-center">
                    <span className="text-white text-sm font-bold">AI</span>
                  </div>
                  <h2 className="text-xl font-semibold">NotebookLM</h2>
                </div>
                <div className="flex items-center gap-2">
                  <Button variant="outline" size="sm">
                    <Search className="w-4 h-4 mr-2" />
                    探索来源
                  </Button>
                  <Button variant="ghost" size="sm" onClick={() => setShowAddSourceModal(false)}>
                    <X className="w-4 h-4" />
                  </Button>
                </div>
              </div>

              {/* Content */}
              <div className="p-6 overflow-y-auto max-h-[calc(90vh-120px)]">
                <div className="mb-6">
                  <h3 className="text-lg font-semibold mb-2">添加来源</h3>
                  <p className="text-sm text-muted-foreground mb-1">
                    添加来源后，NotebookLM 能够基于这些对您最重要的信息提供回答。
                  </p>
                  <p className="text-sm text-muted-foreground">
                    (示例：营销方案、课程阅读材料、研究笔记、会议转写内容、销售文档等)
                  </p>
                </div>

                {/* Upload Area */}
                <div className="border-2 border-dashed border-border rounded-lg p-12 mb-6 text-center">
                  <div className="flex flex-col items-center gap-4">
                    <Upload className="w-12 h-12 text-muted-foreground" />
                    <div>
                      <h4 className="font-medium mb-2">上传来源</h4>
                      <p className="text-sm text-muted-foreground mb-2">拖放或选择文件，即可上传</p>
                      <p className="text-xs text-muted-foreground">
                        支持的文件类型：PDF, txt, Markdown, 音频 (例如 mp3)
                      </p>
                    </div>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4 mb-6">
                  {/* Links */}
                  <Card className="p-4 hover:bg-accent cursor-pointer transition-colors">
                    <div className="flex items-center gap-3">
                      <Link className="w-8 h-8 text-muted-foreground" />
                      <span className="font-medium">链接</span>
                    </div>
                  </Card>

                  {/* Paste Text */}
                  <Card className="p-4 hover:bg-accent cursor-pointer transition-colors" onClick={handlePasteTextClick}>
                    <div className="flex items-center gap-3">
                      <FileText className="w-8 h-8 text-muted-foreground" />
                      <span className="font-medium">粘贴文字</span>
                    </div>
                  </Card>
                </div>

                {/* Source Limit Toggle */}
                <div className="flex items-center justify-between p-4 bg-accent rounded-lg">
                  <div className="flex items-center gap-3">
                    <FileText className="w-5 h-5 text-muted-foreground" />
                    <span className="font-medium">来源限制</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <div className="w-10 h-6 bg-primary rounded-full relative">
                      <div className="w-4 h-4 bg-white rounded-full absolute top-1 right-1"></div>
                    </div>
                    <span className="text-sm text-muted-foreground">1/50</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {showPasteTextModal && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm">
          <div className="absolute inset-0 flex items-center justify-center p-4">
            <div
              className="rounded-lg border border-border w-full max-w-4xl max-h-[90vh] overflow-hidden"
              style={{ backgroundColor: "rgb(35, 38, 43)" }}
            >
              {/* Header */}
              <div className="flex items-center justify-between p-6 border-b border-border">
                <div className="flex items-center gap-3">
                  <Button variant="ghost" size="sm" onClick={handleClosePasteTextModal}>
                    <ArrowLeft className="w-4 h-4" />
                  </Button>
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 bg-gradient-to-br from-orange-500 to-red-500 rounded-full flex items-center justify-center">
                      <span className="text-white text-sm font-bold">AI</span>
                    </div>
                    <h2 className="text-xl font-semibold">NotebookLM</h2>
                  </div>
                </div>
                <Button variant="ghost" size="sm" onClick={() => setShowPasteTextModal(false)}>
                  <X className="w-4 h-4" />
                </Button>
              </div>

              {/* Content */}
              <div className="p-6 overflow-y-auto max-h-[calc(90vh-180px)]">
                <div className="mb-6">
                  <h3 className="text-lg font-semibold mb-2">粘贴已复制的文字</h3>
                  <p className="text-sm text-muted-foreground">
                    将复制的文字粘贴到下方，以便在 NotebookLM 中作为来源上传
                  </p>
                </div>

                {/* Text Input Area */}
                <div className="mb-6">
                  <Textarea
                    placeholder="在此处粘贴文字"
                    value={pasteTextValue}
                    onChange={(e) => setPasteTextValue(e.target.value)}
                    className="min-h-[300px] resize-none"
                  />
                </div>
              </div>

              {/* Footer */}
              <div className="flex items-center justify-end gap-3 p-6 border-t border-border">
                <Button variant="outline" onClick={handleClosePasteTextModal}>
                  取消
                </Button>
                <Button onClick={handleInsertText} disabled={!pasteTextValue.trim()}>
                  插入
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
