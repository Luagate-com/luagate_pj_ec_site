INSERT INTO goods (id, code, name, description, price, category, image_url, created_at, updated_at) VALUES
  (1, 'ZAK-001', 'アロマキャンドル', '植物由来の香りでリラックスタイムを演出するキャンドル。', 2480, 'インテリア', '/assets/images/goods/aroma_candle.jpg', NOW(), NOW()),
  (2, 'ZAK-002', 'ガラスフラワーベース', '透明感のあるガラス素材で花を引き立てるベース。', 1980, 'インテリア', '/assets/images/goods/glass_flower_vase.jpg', NOW(), NOW()),
  (3, 'ZAK-003', '木製カトラリーセット', '天然木の質感が食卓になじむカトラリーセット。', 1320, 'キッチン雑貨', '/assets/images/goods/wooden_cutlery_set.jpg', NOW(), NOW()),
  (4, 'ZAK-004', 'リネンポーチ', 'ナチュラルな風合いのリネン素材ポーチ。', 1760, 'ファブリック', '/assets/images/goods/linen_pouch.jpg', NOW(), NOW()),
  (5, 'ZAK-005', 'レタープレス便箋', '上質紙に凹凸が美しいレタープレス便箋。', 880, '文具', '/assets/images/goods/letterpress_stationery.jpg', NOW(), NOW()),
  (6, 'ZAK-006', 'ミニ観葉植物セット', '小さなグリーンをまとめた室内用セット。', 2200, 'インテリア', '/assets/images/goods/mini_houseplant_set.jpg', NOW(), NOW()),
  (7, 'ZAK-007', 'エコバッグ', '折りたたみできる軽量エコバッグ。', 1200, 'ファッション', '/assets/images/goods/eco_bag.jpg', NOW(), NOW()),
  (8, 'ZAK-008', 'アロマディフューザー', 'やさしく香りを広げるアロマディフューザー。', 3500, 'インテリア', '/assets/images/goods/aroma_diffuser.jpg', NOW(), NOW()),
  (9, 'ZAK-009', 'ハンドメイドキャンドル', '手作りならではの温かみがあるキャンドル。', 1500, 'インテリア', '/assets/images/goods/handmade_candle.jpg', NOW(), NOW()),
  (10, 'ZAK-010', '陶器のマグカップ', '日常使いしやすい陶器のマグカップ。', 1000, '食器', '/assets/images/goods/ceramic_mug.jpg', NOW(), NOW()),
  (11, 'ZAK-011', 'キャンバスアート', 'インテリアに映えるキャンバスアート。', 5000, 'インテリア', '/assets/images/goods/canvas_art.jpg', NOW(), NOW()),
  (12, 'ZAK-012', 'オーガニックティーセット', '香り高い茶葉を詰め合わせたティーセット。', 2800, 'キッチン雑貨', '/assets/images/goods/organic_tea_set.jpg', NOW(), NOW());

INSERT INTO stocks (good_id, quantity, updated_at) VALUES
  (1, 50, NOW()),
  (2, 30, NOW()),
  (3, 100, NOW()),
  (4, 20, NOW()),
  (5, 40, NOW()),
  (6, 60, NOW()),
  (7, 25, NOW()),
  (8, 35, NOW()),
  (9, 15, NOW()),
  (10, 80, NOW()),
  (11, 20, NOW()),
  (12, 40, NOW());

-- 明示的にIDを挿入しているため、次回採番値を現在の最大IDに合わせる。
SELECT setval(pg_get_serial_sequence('goods', 'id'), COALESCE((SELECT MAX(id) FROM goods), 1), true);
